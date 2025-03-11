import { useState, useEffect } from 'react';
import axios from 'axios';
import { Button, Form, Input, Modal, message, Upload, Steps, Alert, Spin } from 'antd';
import { GOOGLE_API_KEY, pharmacyURL, URL } from '../../Api & Services/Api';
import { AimOutlined, LoadingOutlined, UploadOutlined } from '@ant-design/icons';
import { GoogleMap, Marker, useJsApiLoader } from '@react-google-maps/api';
import PharmacyInfo from './PharmacyInfo';
import { generateLabel } from '../../Api & Services/Services';
import "./Pharmacy.css"
import { fetchPharmacyProfileData } from './fetchPharmacistProfileData';

const googleMapsLibraries = ['places'];

const Pharmacy = () => {
  const [Pharmacy, setPharmacy] = useState(null);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [load, setLoad] = useState(false);
  const [map, setMap] = useState(null); 
  const [currentLocation, setCurrentLocation] = useState({lat:25.5750, lng:78.8185});
  const [selectedLocation, setSelectedLocation] = useState(null);
  const [currentStep, setCurrentStep] = useState(0);
  const [newPharmacy, setNewPharmacy] = useState({
    pharmacyName: '',
    overview: '',
    services: '',
    pharmacyTechnology: '',
    accreditations: '',
    insurancePartners: [],
    address: {
      street: '',
      city: '',
      state: '',
      zip: '',
      country: '',
      latitude: '',
      longitude: '',
    },
    email: '',
    mobile: '',
    website: '',
    openingTime: '',
    closingTime: '',
    images: [],
  });  
  const [fileList, setFileList] = useState([]);
  const { Step } = Steps;

  const { isLoaded } = useJsApiLoader({
    googleMapsApiKey: `${GOOGLE_API_KEY}`,
    libraries: googleMapsLibraries,
  });

  const fetchPharmacy = async () => {
    await fetchPharmacyProfileData(setPharmacy, setLoading, setError);
  };

  useEffect(() => {
    fetchPharmacy();
  }, []);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewPharmacy((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };
  
  const handleAddressChange = (e) => {
    const { name, value } = e.target;
    setNewPharmacy((prevState) => ({
      ...prevState,
      address: {
        ...prevState.address,
        [name]: value,
      },
    }));
  };


  const handleImageChange = ({ fileList }) => {
    setFileList(fileList);
    const images = fileList.map((file) => file.response);
    setNewPharmacy((prevState) => ({
      ...prevState,
      images,
    }));
  };

  const handleRemoveImage = async (file) => {
    try {
      const response = await axios.delete(`${URL}/delete-image`, {
        data: {
          fileName: file.name,
        },
      });
      setFileList((prevList) => prevList.filter((item) => item.uid !== file.uid));
      setNewPharmacy((prevState) => ({
        ...prevState,
        images: prevState.images.filter((image) => image !== file.thumbUrl),
      }));
      message.success(response.data);
    } catch (error) {
      if(error.response?.data?.message){
        message.error(error.response.data.message);
      } else{
        message.error('An error occurred, please try again');
      }
      console.error("Error in deleting file: ", error);
    }
  };

  const handleModalOpen = () => {
    setIsModalVisible(true);
  };

  const handleModalClose = () => {
    setIsModalVisible(false);
    setNewPharmacy({
      pharmacyName: '',
    overview: '',
    services: '',
    pharmacyTechnology: '',
    accreditations: '',
    insurancePartners: [],
    address: {
      street: '',
      city: '',
      state: '',
      zip: '',
      country: '',
      latitude: '',
      longitude: '',
    },
    email: '',
    mobile: '',
    website: '',
    openingTime: '',
    closingTime: '',
    images: [],
    });
    setFileList([]);
  };

  const handleRegisterPharmacy = async () => {
    setLoad(true);
    console.log(newPharmacy);
    if (newPharmacy.images.length === 0) {
      message.error("Please Upload images");
      return;
    }    
    console.log(newPharmacy);
    try {
      const token = localStorage.getItem('token');
      const response = await axios.post(`${pharmacyURL}/register`, newPharmacy, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      message.success(response.data?.message);
      setIsModalVisible(false);
      await fetchPharmacy();
    } catch (error) {
      if(error.response?.data?.message){
        message.error(error.response.data.message);
      } else{
        message.error('An error occurred, please try again');
      }
      console.error("Error in registering Pharmacy: ", error);
    } finally {
      setLoad(false);
    }
  };

  const handleMapClick = (e) => {
    const lat = e.latLng.lat();
    const lng = e.latLng.lng();
    
    setSelectedLocation({lat, lng});
    setNewPharmacy(prevState => ({
      ...prevState,
      address: {
        ...prevState.address,
        latitude: lat,
        longitude: lng,
      }
    }));
  
    const geocoder = new window.google.maps.Geocoder();
    geocoder.geocode({ location: { lat, lng } }, (results, status) => {
      if (status === 'OK' && results[0]) {
        const addressComponents = results[0].address_components;
        let street = '', city = '', state = '', zip = '', country = '';
  
        addressComponents.forEach(component => {
          if (component.types.includes('street_number') || component.types.includes('route')) street += component.long_name + ' ';
          if (component.types.includes('locality')) city = component.long_name;
          if (component.types.includes('administrative_area_level_1')) state = component.long_name;
          if (component.types.includes('postal_code')) zip = component.long_name;
          if (component.types.includes('country')) country = component.long_name;
        });
  
        setNewPharmacy(prevState => ({
          ...prevState,
          address: {
            ...prevState.address,
            street,
            city,
            state,
            zip,
            country,
          }
        }));
      }
    });
  };
  

  const handleRecenterMap = () => {
    if (navigator.geolocation && map) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          const { latitude, longitude } = position.coords;
          const location = { lat: latitude, lng: longitude };
          
          setCurrentLocation(location);
          setSelectedLocation(location);
          map.panTo(location);
          
          setNewPharmacy(prevState => ({
            ...prevState,
            address: {
              ...prevState.address,
              latitude: location.lat,
              longitude: location.lng,
            }
          }));
  
          const geocoder = new window.google.maps.Geocoder();
          geocoder.geocode({ location }, (results, status) => {
            if (status === 'OK' && results[0]) {
              const addressComponents = results[0].address_components;
              let street = '', city = '', state = '', zip = '', country = '';
  
              addressComponents.forEach(component => {
                if (component.types.includes('street_number') || component.types.includes('route')) street += component.long_name + ' ';
                if (component.types.includes('locality')) city = component.long_name;
                if (component.types.includes('administrative_area_level_1')) state = component.long_name;
                if (component.types.includes('postal_code')) zip = component.long_name;
                if (component.types.includes('country')) country = component.long_name;
              });
  
              setNewPharmacy(prevState => ({
                ...prevState,
                address: {
                  ...prevState.address,
                  street,
                  city,
                  state,
                  zip,
                  country,
                }
              }));
            }
          });
        },
        (error) => {
          message.error("Error getting location");
          console.error(error);
        }
      );
    }
  };

  const validateStep = () => {
    const stepValidation = [
      // Step 1: Basic Details Validation
      () => {
        if (!newPharmacy.pharmacyName) return { valid: false, error: "Pharmacy name is required." };
        if (!newPharmacy.mobile) return { valid: false, error: "Mobile number is required." };
        if (!/^[0-9]{10}$/.test(newPharmacy.mobile)) return { valid: false, error: "Invalid mobile number. Must be exactly 10 digits." };
        if (!newPharmacy.email) return { valid: false, error: "Email is required." };
        if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(newPharmacy.email)) return { valid: false, error: "Invalid email format." };
        if (!newPharmacy.website) return { valid: false, error: "Website URL is required." };
        if (!/^(http|https):\/\/[^ "]+$/.test(newPharmacy.website)) return { valid: false, error: "Invalid website URL format." };
        if (!newPharmacy.openingTime) return { valid: false, error: "Opening time is required." };
        if (!newPharmacy.closingTime) return { valid: false, error: "Closing time is required." };

        return { valid: true };
      },

      // Step 2: Accreditation & Services Validation
      () => {
        if (!newPharmacy.accreditations) return { valid: false, error: "Accreditations are required." };
        if (!newPharmacy.insurancePartners.length) return { valid: false, error: "At least one insurance partner is required." };
        if (!newPharmacy.overview) return { valid: false, error: "Overview is required." };
        if (!newPharmacy.services) return { valid: false, error: "Services details are required." };
        if (!newPharmacy.pharmacyTechnology) return { valid: false, error: "Pharmacy technology details are required." };

        return { valid: true };
      },

      // Step 3: Location Validation
      () => {
        if (!newPharmacy.address.latitude) return { valid: false, error: "Latitude is required." };
        if (!newPharmacy.address.longitude) return { valid: false, error: "Longitude is required." };

        return { valid: true };
      },

      // Step 4: Address Validation
      () => {
        if (!newPharmacy.address.street) return { valid: false, error: "Street address is required." };
        if (!newPharmacy.address.city) return { valid: false, error: "City is required." };
        if (!newPharmacy.address.state) return { valid: false, error: "State is required." };
        if (!newPharmacy.address.zip) return { valid: false, error: "ZIP code is required." };
        if (!/^[0-9]{5,6}$/.test(newPharmacy.address.zip)) return { valid: false, error: "Invalid ZIP code. Must be 5 or 6 digits." };
        if (!newPharmacy.address.country) return { valid: false, error: "Country is required." };

        return { valid: true };
      },

      // Step 5: Image Upload Validation
      () => {
        if (!newPharmacy.images.length) return { valid: false, error: "At least one image must be uploaded." };

        return { valid: true };
      },
    ];

    return stepValidation[currentStep]();
  };

  const nextStep = () => {
    const validationResult = validateStep();

    if (!validationResult.valid) {
      message.error(validationResult.error); // Show specific validation error
      return;
    }

    setCurrentStep(currentStep + 1);
  };

  const prevStep = () => setCurrentStep(currentStep - 1);

  const steps = [
    {
      title: 'Basic Details',
      content: (
        <Form layout="vertical">
          {['pharmacyName', 'email', 'mobile', 'website', 'openingTime', 'closingTime'].map((field, index) => (
            <Form.Item 
            key={index}
            label={generateLabel(field)}
            >   
              <Input
                name={field}
                value={newPharmacy[field]}
                onChange={handleInputChange}
                type={field === 'mobile'? 'number' : field === 'email' ? 'email' : field === 'openingTime' || field === 'closingTime' ? 'time' : 'text'}
              />
            </Form.Item>
          ))}
        </Form>
      ),
    },
    {
      title: 'Description',
      content: (
        <Form layout="vertical">
          {['overview', 'services', 'pharmacyTechnology', 'accreditations', 'insurancePartners'].map((field, index) =>
            <Form.Item 
              key={index}
              label={generateLabel(field)}
            >  
              {field === "insurancePartners" ?  
                <Input.TextArea
                  value={newPharmacy.insurancePartners.join(", ")}
                  onChange={(e) => {
                    const { value } = e.target;
                    setNewPharmacy((prevState) => ({
                      ...prevState,
                      insurancePartners: value ? value.split(",").map(item => item.trim()) : [],
                    }));
                  }}
                  placeholder="Enter insurancePartners separated by commas"
                  rows={4}
                /> : 
                <Input.TextArea
                  name={field}
                  value={newPharmacy[field]}
                  onChange={handleInputChange}
                  rows={4}
                  placeholder={field === "overview" ? "Enter in paragraph" : "Enter pointwise with heading separated by ':' and ending with the '.'"}
                />
              }
            </Form.Item>
          )}
        </Form>
      )
    },
    {
      title: 'Location',
      content: (
        <Form layout="vertical">
          <Form.Item label="Latitude and Longitude">
          {isLoaded ? (
              <GoogleMap
                onLoad={(mapInstance) => setMap(mapInstance)}
                mapContainerStyle={{ width: '100%', height: '400px' }}
                center={selectedLocation? (selectedLocation) : (currentLocation)}
                zoom={currentLocation ? 14 : 2}
                mapContainerClassName='google-map'
                onClick={handleMapClick}
                options={{
                  fullscreenControl: false,
                  streetViewControl: false,
                  mapTypeControl: false,
                }}
              >
                {selectedLocation && <Marker position={selectedLocation} />}
                <Button
                  variant="contained"
                  type='default'
                  onClick={handleRecenterMap}
                  style={{ margin: 1}}
                >
                  <AimOutlined style={{ fontSize: '24px' }}/>
                </Button>
              </GoogleMap>
            ) : (
              <div className='loader'><LoadingOutlined/></div>
            )}
          </Form.Item>
        </Form>
      ),
    },
    {
      title: 'Address',
      content: (
        <Form layout="vertical">
          {['street', 'city', 'state', 'zip', 'country'].map((field) => (
            <Form.Item
              label={generateLabel(field)}
              key={field}
            >
              <Input
                name={field}
                value={newPharmacy.address[field]}
                onChange={handleAddressChange}
              />
            </Form.Item>
          ))}
        </Form>
      ),
    },
    {
      title: 'Images',
      content: (
        <Form layout="vertical">
          <Form.Item label="Pharmacy Images">
            <Upload
              action={`${URL}/upload-image`}
              listType="picture-card"
              fileList={fileList}
              onChange={handleImageChange}
              onRemove={handleRemoveImage}
              accept="image/*"
              multiple
              showUploadList={{ showRemoveIcon: true }}
            >
              <Button icon={<UploadOutlined />}>Upload</Button>
            </Upload>
          </Form.Item>
        </Form>
      ),
    },
  ];

  return (
    <div className="Pharmacy-details">
      {loading ? (
      <Spin
        tip="Loading Doctors..."
        size="large"
        style={{ display: "block", margin: "auto" }}
      />
        ) : error ? (
          <Alert message={error} type="error" showIcon/>
        ) : (
      Pharmacy ? (
        <PharmacyInfo Pharmacy = {Pharmacy} refreshPharmacyData={fetchPharmacy}/>
      ) : (
        <div className="no-Pharmacy-message">
          <Alert
            message="No Pharmacy found"
            description="Please register your Pharmacy to continue"
            type="error"
            action ={
              <Button danger type="primary" onClick={handleModalOpen}>
                Register Pharmacy
              </Button>
            }
          />
        </div>
      ))}

      <Modal
        title="Register Pharmacy"
        open={isModalVisible}
        onOk={handleRegisterPharmacy}
        onCancel={handleModalClose}
        confirmLoading={load}
        footer={null}
        className="Pharmacy-register-modal"
        width={600}
      >
        <Steps current={currentStep} style={{marginBottom: 10}}>
          {steps.map((item) => (
            <Step key={item.title} title={item.title} />
          ))}
        </Steps>
        <div className="steps-content">{steps[currentStep].content}</div>
        <div className="steps-action">
          {currentStep > 0 && (
            <Button style={{ marginRight: '8px' }} onClick={prevStep}>
              Previous
            </Button>
          )}
          {currentStep < steps.length - 1 && (
            <Button type="primary" onClick={nextStep}>
              Next
            </Button>
          )}
          {currentStep === steps.length - 1 && (
            <Button type="primary" onClick={handleRegisterPharmacy} loading={load}>
              Submit
            </Button>
          )}
        </div>
      </Modal>
    </div>
  );
};

export default Pharmacy;
