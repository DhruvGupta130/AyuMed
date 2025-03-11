import { useState, useEffect } from 'react';
import { Alert, Button, Dialog, DialogActions, DialogContent, DialogTitle, TextField } from '@mui/material';
import { Home, LocationOn, MyLocation } from '@mui/icons-material';
import { GoogleMap, useJsApiLoader, Marker } from '@react-google-maps/api';
import {GOOGLE_API_KEY, patientURL} from "../../Api & Services/Api.js";
import axios from 'axios';
import PropTypes from 'prop-types';

const GoogleMapLibraries = ['places'];

const PatientAddressCard = ({ patient, refreshProfileData }) => {
  const [isEditing, setIsEditing] = useState(false);
  const [map, setMap] = useState(null); 
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(null);
  const [address, setAddress] = useState({
    street: '',
    city: '',
    state: '',
    zip: '',
    country: '',
    latitude: null,
    longitude: null,
  });
  const [currentLocation, setCurrentLocation] = useState(null);
  const [selectedLocation, setSelectedLocation] = useState(null);
  const [showForm, setShowForm] = useState(false);

  const { isLoaded } = useJsApiLoader({
    googleMapsApiKey: `${GOOGLE_API_KEY}`,
    libraries: GoogleMapLibraries,
  });

  useEffect(() => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          const { latitude, longitude } = position.coords;
          const location = { lat: latitude, lng: longitude };
          setCurrentLocation(location);
        },
        (error) => {
          setError("Error getting location");
          console.error(error);
        }
      );
    } else {
      setError("Geolocation is not supported by this browser.");
    }
  }, []);

  useEffect(()=>{
    if(patient.address){
      setAddress(patient.address);
    }
  },[patient.address]);

  useEffect(() => {
    if (success) {
      const successTimeout = setTimeout(() => {
        setSuccess('');
        setIsEditing(false);
        refreshProfileData();
      }, 2000);
      return () => clearTimeout(successTimeout); 
    }
  }, [refreshProfileData, success]);

  useEffect(() => {
    if (error) {
      const errorTimeout = setTimeout(() => {
        setError('');
      }, 2000);
      return () => clearTimeout(errorTimeout);
    }
  }, [error]);

  const handleClickOpen = () => {
    setIsEditing(true);
    setShowForm(false);
  };

  const handleClose = () => {
    setIsEditing(false);
    setSelectedLocation(null);
    setShowForm(false);
  };

  const handleViewAddress = () => {
    const { latitude, longitude } = patient.address || {};
    if (latitude && longitude) {
      const mapsUrl = `https://www.google.com/maps?q=${latitude},${longitude}`;
      window.open(mapsUrl, '_blank');
    } else {
      setError("Location not available");
    }
  }

  const handleMapClick = (e) => {
    const lat = e.latLng.lat();
    const lng = e.latLng.lng();
  
    setSelectedLocation({ lat, lng });
    setAddress({ ...address, latitude: lat, longitude: lng });
 
    if (!window.google?.maps?.Geocoder) {
      console.error('Google Maps Geocoder API is not loaded');
      return;
    }
  
    const geocoder = new window.google.maps.Geocoder();
  
    geocoder.geocode({ location: { lat, lng } }, (results, status) => {
      if (status === 'OK' && results[0]) {
        const addressComponents = results[0].address_components;
        let street = '', city = '', state = '', zip = '', country = '';
  
        addressComponents.forEach(component => {
          if (component.types.includes('street_number') || component.types.includes('route')) {
            street += component.long_name + ' ';
          }
          if (component.types.includes('locality')) {
            city = component.long_name;
          }
          if (component.types.includes('administrative_area_level_1')) {
            state = component.long_name;
          }
          if (component.types.includes('postal_code')) {
            zip = component.long_name;
          }
          if (component.types.includes('country')) {
            country = component.long_name;
          }
        });

        console.log(addressComponents);

        setAddress(prevState => ({
          ...prevState,
          street,
          city,
          state,
          zip,
          country,
        }));
      } else {
        console.error('Geocoding failed:', status);
      }
    });
  };  

  const handleSubmit = async () => {
    setLoading(true);
    if(!address.latitude || !address.longitude){
      setError("Please Select your location on the map");
      return;
    }
    if(!address.city || !address.country || 
      !address.state || !address.street || !address.zip){
        setError("Please Fill all the required fields");
        return;
    }


    try {
      const token = localStorage.getItem('token');
      const method = patient.address ? 'put' : 'post';
      const response = await axios[method](`${patientURL}/address`, address, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setIsEditing(false);
      refreshProfileData();
      setSuccess(response.data.message);
    } catch (error) {
      if(error.response?.data?.message){
        setError(error.response.data.message);
      } else{
        setError('An error occurred, please try again');
      }
      console.error("Error in changing address: ", error);
    } finally {
      setLoading(false);
    }
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
          setAddress(prevState => ({
            ...prevState,
            latitude: location.lat,
            longitude: location.lng
          }));
  
          const geocoder = new window.google.maps.Geocoder();
          geocoder.geocode({ location: location }, (results, status) => {
            if (status === 'OK' && results[0]) {
              const addressComponents = results[0].address_components;
              let street = '', city = '', state = '', zip = '', country = '';
  
              addressComponents.forEach(component => {
                if (component.types.includes('street_number') || component.types.includes('route')) {
                  street += component.long_name + ' ';
                }
                if (component.types.includes('locality')) {
                  city = component.long_name;
                }
                if (component.types.includes('administrative_area_level_1')) {
                  state = component.long_name;
                }
                if (component.types.includes('postal_code')) {
                  zip = component.long_name;
                }
                if (component.types.includes('country')) {
                  country = component.long_name;
                }
              });
  
              setAddress(prevState => ({
                ...prevState,
                street,
                city,
                state,
                zip,
                country
              }));
            } else {
              console.error('Geocoding failed:', status);
            }
          });
        },
        (error) => {
          setError("Error getting location");
          console.error(error);
        }
      );
    }
  };  
  
  return (
    <div className="patient-card">
      <h2>Address</h2>
      <div className="patient-info">
        <p><strong>Street:</strong> {patient.address?.street}</p>
        <p><strong>City:</strong> {patient.address?.city}</p>
        <p><strong>State:</strong> {patient.address?.state}</p>
        <p><strong>Postal Code:</strong> {patient.address?.zip}</p>
        <p><strong>Country:</strong> {patient.address?.country}</p>
      </div>
      <div className="maps-button">
        {patient.address && <Button variant="outlined" color="secondary" startIcon={<LocationOn />} sx={{m:0.5}} onClick={handleViewAddress}>
            View on Maps
          </Button>}
        <Button variant="outlined" color="info" startIcon={<Home />} sx={{m:0.5}} onClick={handleClickOpen}>
          Update Address
        </Button>
      </div>

      {/* Address Update Modal */}
      <Dialog open={isEditing} onClose={handleClose} className='form'>
        <DialogTitle sx={{textAlign:'center', fontSize:'x-large'}}>{!showForm ?`Choose Location`: `Enter Address`}</DialogTitle>
        {error && <Alert severity='error' className='error-message' sx={{marginX: 2}}>{error}</Alert>}
        {success && <Alert severity='success' className='success-message' sx={{marginX: 2}}>{success}</Alert>}
        <DialogContent>
          {!showForm && <div className='map-container'>
            {isLoaded ? (
              <GoogleMap
                onLoad={(mapInstance) => setMap(mapInstance)}
                center={(currentLocation)}
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
                  color="alert"
                  onClick={handleRecenterMap}
                  style={{ margin: 1}}
                >
                  <MyLocation/>
                </Button>
              </GoogleMap>
            ) : (
              <div>Loading map...</div>
            )}
          </div>}

          {/* Address Form */}
          {showForm && (
            <>
              <TextField
                label="Street"
                name="street"
                value={address.street}
                onChange={(e) => setAddress({ ...address, street: e.target.value })}
                fullWidth
                margin="normal"
              />
              <TextField
                label="City"
                name="city"
                value={address.city}
                onChange={(e) => setAddress({ ...address, city: e.target.value })}
                fullWidth
                margin="normal"
              />
              <TextField
                label="State"
                name="state"
                value={address.state}
                onChange={(e) => setAddress({ ...address, state: e.target.value })}
                fullWidth
                margin="normal"
              />
              <TextField
                label="Postal Code"
                name="zip"
                value={address.zip}
                onChange={(e) => setAddress({ ...address, zip: e.target.value })}
                fullWidth
                margin="normal"
              />
              <TextField
                label="Country"
                name="country"
                value={address.country}
                onChange={(e) => setAddress({ ...address, country: e.target.value })}
                fullWidth
                margin="normal"
              />
            </>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose} color="secondary">
            Cancel
          </Button>
          {showForm ? (<Button onClick={handleSubmit} loading={loading} color="primary">
            Save
          </Button>):(
            <Button onClick={()=> setShowForm(true)} color="primary">
            Next
          </Button>
          )}
        </DialogActions>
      </Dialog>
    </div>
  );
};

PatientAddressCard.propTypes = {
  patient: PropTypes.shape({
    address: PropTypes.shape({
      street: PropTypes.string,
      city: PropTypes.string,
      state: PropTypes.string,
      zip: PropTypes.string,
      country: PropTypes.string,
      latitude: PropTypes.number,
      longitude: PropTypes.number,
    }),
  }).isRequired,
  refreshProfileData: PropTypes.func.isRequired,
};

export default PatientAddressCard;
