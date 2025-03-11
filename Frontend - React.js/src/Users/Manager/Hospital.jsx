import { useState, useEffect } from 'react';
import axios from 'axios';
import { Button, Form, Input, Modal, message, Upload, Steps, Alert, Spin, Checkbox } from 'antd';
import { GOOGLE_API_KEY, hospitalURL, URL } from "../../Api & Services/Api.js";
import { AimOutlined, LoadingOutlined, UploadOutlined } from '@ant-design/icons';
import { GoogleMap, Marker, useJsApiLoader } from '@react-google-maps/api';
import HospitalInfo from './HospitalInfo';
import { generateLabel } from '../../Api & Services/Services.js';
import { fetchHospitalProfileData } from './fetchManagerProfileData.jsx';

const googleMapsLibraries = ['places'];

const Hospital = () => {
  const [hospital, setHospital] = useState(null);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [load, setLoad] = useState(false);
  const [map, setMap] = useState(null); 
  const [currentLocation, setCurrentLocation] = useState({lat:25.5750, lng:78.8185});
  const [selectedLocation, setSelectedLocation] = useState(null);
  const [currentStep, setCurrentStep] = useState(0);
  const [newHospital, setNewHospital] = useState({
    hospitalName: '',
    email: '',
    mobile: '',
    website: '',
    establishedYear: '',
    overview: '',
    specialities: [],
    emergencyServices: false,
    bedCapacity: '',
    icuCapacity: '',
    operationTheaters: '',
    technology: '',
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
    images: [],
  });
  const [fileList, setFileList] = useState([]);
  const { Step } = Steps;

  const { isLoaded } = useJsApiLoader({
    googleMapsApiKey: `${GOOGLE_API_KEY}`,
    libraries: googleMapsLibraries,
  });

  const fetchHospital = async () => {
    await fetchHospitalProfileData(setHospital, setLoading, setError);
  };

  useEffect(() => {
    fetchHospital();
  }, []);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewHospital((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const handleCheckInput = (e) => {
    const { name, checked } = e.target;
    setNewHospital((prevState) => ({
      ...prevState,
      [name]: checked,
    }));
  }
  
  const handleAddressChange = (e) => {
    const { name, value } = e.target;
    setNewHospital((prevState) => ({
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
    setNewHospital((prevState) => ({
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
      setNewHospital((prevState) => ({
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
    setNewHospital({
      hospitalName: '',
      email: '',
      mobile: '',
      website: '',
      establishedYear: '',
      overview: '',
      specialities: [],
      emergencyServices: '',
      bedCapacity: '',
      icuCapacity: '',
      operationTheaters: '',
      technology: '',
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
      images: [],
    });
    setFileList([]);
  };

  const handleRegisterHospital = async () => {
    console.log(newHospital);
    setLoad(true);
    if (newHospital.images.length === 0) {
      message.error("Please Upload images");
      return;
    }
    
    try {
      const token = localStorage.getItem('token');
      const response = await axios.post(`${hospitalURL}/register`, newHospital, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      message.success(response.data?.message);
      setIsModalVisible(false);
      await fetchHospital();
    } catch (error) {
      if(error.response?.data?.message){
        message.error(error.response.data.message);
      } else{
        message.error('An error occurred, please try again');
      }
      console.error("Error in registering hospital: ", error);
    } finally {
      setLoad(false);
    }
  };

  const handleMapClick = (e) => {
    const lat = e.latLng.lat();
    const lng = e.latLng.lng();
    
    setSelectedLocation({lat, lng});
    setNewHospital(prevState => ({
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
  
        setNewHospital(prevState => ({
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
          
          setNewHospital(prevState => ({
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
  
              setNewHospital(prevState => ({
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
        if (!newHospital.hospitalName) return { valid: false, error: "Hospital name is required." };
        if (!newHospital.mobile) return { valid: false, error: "Mobile number is required." };
        if (!/^[0-9]{10}$/.test(newHospital.mobile)) return { valid: false, error: "Invalid mobile number. Must be exactly 10 digits." };
        if (!newHospital.email) return { valid: false, error: "Email is required." };
        if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(newHospital.email)) return { valid: false, error: "Invalid email format." };
        if (!newHospital.website) return { valid: false, error: "Website URL is required." };
        if (!/^(http|https):\/\/[^ "]+$/.test(newHospital.website)) return { valid: false, error: "Invalid website URL format." };
        if (!newHospital.establishedYear) return { valid: false, error: "Established year is required." };
        if (!/^[0-9]{4}$/.test(newHospital.establishedYear)) return { valid: false, error: "Invalid established year format." };

        return { valid: true };
      },

      // Step 2: Description Validation
      () => {
        if (!newHospital.overview) return { valid: false, error: "Hospital overview is required." };
        if (!newHospital.specialities.length) return { valid: false, error: "At least one speciality is required." };
        if (typeof newHospital.emergencyServices !== "boolean") return { valid: false, error: "Emergency services must be true or false." };
        if (!newHospital.technology) return { valid: false, error: "Technology details are required." };

        return { valid: true };
      },

      // Step 3: More Details Validation
      () => {
        if (!newHospital.bedCapacity) return { valid: false, error: "Bed capacity is required." };
        if (!newHospital.icuCapacity) return { valid: false, error: "ICU capacity is required." };
        if (!newHospital.operationTheaters) return { valid: false, error: "Number of operation theaters is required." };
        if (!newHospital.accreditations) return { valid: false, error: "Accreditations are required." };
        if (!newHospital.insurancePartners.length) return { valid: false, error: "At least one insurance partner is required." };

        return { valid: true };
      },

      // Step 4: Location Validation
      () => {
        if (!newHospital.address.latitude) return { valid: false, error: "Latitude is required." };
        if (!newHospital.address.longitude) return { valid: false, error: "Longitude is required." };

        return { valid: true };
      },

      // Step 5: Address Validation
      () => {
        if (!newHospital.address.street) return { valid: false, error: "Street address is required." };
        if (!newHospital.address.city) return { valid: false, error: "City is required." };
        if (!newHospital.address.state) return { valid: false, error: "State is required." };
        if (!newHospital.address.zip) return { valid: false, error: "ZIP code is required." };
        if (!/^[0-9]{5,6}$/.test(newHospital.address.zip)) return { valid: false, error: "Invalid ZIP code. Must be 5 or 6 digits." };
        if (!newHospital.address.country) return { valid: false, error: "Country is required." };

        return { valid: true };
      },

      // Step 6: Image Upload Validation
      () => {
        if (!newHospital.images.length) return { valid: false, error: "At least one image must be uploaded." };

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
          {['hospitalName', 'mobile', 'email', 'website', 'establishedYear'].map((field) => (
            <Form.Item label={generateLabel(field)} key={field}>
              <Input
                name={field}
                value={newHospital[field]}
                onChange={handleInputChange}
                type={field === 'mobile' || field === 'establishedYear' ? 'number' : field === 'email' ? 'email' : 'text'}
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
          {['overview', 'specialities', 'emergencyServices', 'technology'].map((field) =>
            <Form.Item 
              key={field}
              label={generateLabel(field)}
            >  
              {field === 'emergencyServices'? 
                (<Checkbox
                  value={newHospital.emergencyServices}
                  onChange={handleCheckInput}
                  name={field}
                >
                  Emergency Services Available
                </Checkbox>)
                : 
              field === "specialities" ?  
                <Input.TextArea
                  value={newHospital.specialities.join(", ")}
                  onChange={(e) => {
                    const { value } = e.target;
                    setNewHospital((prevState) => ({
                      ...prevState,
                      specialities: value ? value.split(",").map(item => item.trim()) : [],
                    }));
                  }}
                  placeholder="Enter hospital specialities separated by commas"
                  rows={4}
                /> :
                <Input.TextArea
                  name={field}
                  value={newHospital[field]}
                  onChange={handleInputChange}
                  rows={4}
                  placeholder={field === "overview" ? "Enter in paragraph" : "Enter pointwise with heading separated by ':' and ending with '."}
                />
              }
            </Form.Item>
          )}
        </Form>
      )
    },
    {
      title: 'More Details',
      content: (
        <Form layout="vertical">
          {['bedCapacity', 'icuCapacity', 'operationTheaters', 'accreditations', 'insurancePartners'].map((field) =>
            <Form.Item 
              label={generateLabel(field)}
              key={field}
            >  
              {field === 'bedCapacity' || field === 'icuCapacity' || field === 'operationTheaters'
               ? (
                <Input
                  name={field}
                  value={newHospital[field]}
                  onChange={handleInputChange}
                  type='number'
                  placeholder={`Enter ${generateLabel(field)}`}
                />
              )
              : field === "insurancePartners" ?  
                <Input.TextArea
                  value={newHospital.insurancePartners.join(", ")}
                  onChange={(e) => {
                    const { value } = e.target;
                    setNewHospital((prevState) => ({
                      ...prevState,
                      insurancePartners: value ? value.split(",").map(item => item.trim()) : [],
                    }));
                  }}
                  placeholder="Enter insurancePartners separated by commas"
                  rows={4}
                /> :  
                <Input.TextArea
                  name={field}
                  value={newHospital[field]}
                  onChange={handleInputChange}
                  rows={4}
                  placeholder="Enter pointwise with heading separated by ':' and ending with '."
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
          <Form.Item label="Latitude and Longitude" key="Latitude and Longitude">
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
              label={field.charAt(0).toUpperCase() + field.slice(1)}
              key={field}
            >
              <Input
                name={field}
                value={newHospital.address[field]}
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
          <Form.Item label="Hospital Images" key="Hospital Images">
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

  if(error) {
    return <Alert message={error} type="error" showIcon/>
  }

  return (
    <div className="hospital-details">
      {loading ? (
      <Spin
        tip="Loading Hospital..."
        size="large"
        style={{ display: "block", margin: "auto" }}
      />
        ) : (
      hospital ? (
        <HospitalInfo hospital = {hospital}/>
      ) : (
        <div className="no-hospital-message">
          <Alert
            message="No hospital found"
            description="Please register your hospital to continue"
            type="error"
            action ={
              <Button danger type="primary" onClick={handleModalOpen}>
                Register Hospital
              </Button>
            }
          />
        </div>
      ))}

      <Modal
        title="Register Hospital"
        open={isModalVisible}
        onOk={handleRegisterHospital}
        onCancel={handleModalClose}
        footer={null}
        className="hospital-register-modal"
        width={600}
      >
        <Steps current={currentStep} style={{marginBottom: 5}}>
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
            <Button type="primary" loading={load} onClick={handleRegisterHospital}>
              Submit
            </Button>
          )}
        </div>
      </Modal>
    </div>
  );
};

export default Hospital;
