import {useEffect, useState} from 'react';
import {Alert, Avatar, Box, Button, Modal, TextField, Typography} from '@mui/material';
import {red} from '@mui/material/colors';
import axios from 'axios';
import {patientURL} from "../../Api & Services/Api.js";
import PropTypes from 'prop-types';
import {Image} from '@mui/icons-material';
import ManageAccountsIcon from '@mui/icons-material/ManageAccounts';
import {getAvatarText} from "../../Api & Services/Services.js";
import {BACKEND_URL} from "../../configuration.js";

const PatientDetailsCard = ({ patient, refreshProfileData }) => {
  const [isEditing, setIsEditing] = useState(false);
  const [updatedPatient, setUpdatedPatient] = useState({ ...patient });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);
  const [imageFile, setImageFile] = useState(null);
  const [imagePreview, setImagePreview] = useState(null); 

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

  if (!patient) {
    return <p>Loading patient details...</p>;
  }

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setUpdatedPatient((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const handleImageChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      try {
        const previewURL = (BACKEND_URL || window.webkitURL).createObjectURL(file);
        setImagePreview(previewURL);
        setImageFile(file); 
      } catch (error) {
        console.error("Preview generation failed:", error);
        alert("Image preview is not supported in your environment.");
      }
    }
  };

  const handleSaveChanges = async () => {
    setLoading(true);
    try {
      const token = localStorage.getItem("token");
      const formData = new FormData();
      if(!updatedPatient.aadhaarId || !updatedPatient.alternateMobile){
        setError("Please fill all the required fields!")
        return;
      }if(updatedPatient.aadhaarId.toString().length !== 12){
        setError('Invalid aadhaar number!')
        return;
      }if(updatedPatient.alternateMobile.toString().length !== 10){
        setError('Invalid mobile number!')
        return;
      }
      formData.append("aadhaarId", updatedPatient.aadhaarId);
      formData.append("mobile", updatedPatient.alternateMobile);
      if (!imageFile) {
        setError('Please select a image!');
        return;
      }
      formData.append("image", imageFile);
      const response = await axios.put(`${patientURL}/updateProfile`, formData, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setSuccess(response.data.message);
    } catch (error) {
      if(error.response?.data?.message){
        setError(error.response.data.message);
      } else{
        setError('An error occurred, please try again');
      }
      console.error("Error updating profile", error);
    } finally {
      setLoading(false);
    }
  };

  const isProfileIncomplete = !patient.image;

  return (
    <div className="patient-card">
      <h2>My Profile</h2>
      <div className="patient-header">
        {patient.image ? (
          <img src={patient.image} alt={`${patient.fullName}`} className="patient-image" />
        ) : (
          <Avatar className='patient-image' variant='rounded' sx={{ bgcolor: red[700]}}>
            {getAvatarText(patient.fullName)}
          </Avatar>
        )}
      </div>
      <div className="patient-info">
        <p><strong>Name:</strong> {patient.fullName}</p>
        <p><strong>Date of Birth:</strong> {new Date(patient.dateOfBirth).toLocaleDateString()}</p>
        <p><strong>Gender:</strong> {patient.gender.charAt(0) + patient.gender.substring(1).toLowerCase()}</p>
        <p><strong>Mobile:</strong> {patient.mobile}</p>
        <p><strong>Alternate Mobile:</strong> {patient.alternateMobile}</p>
        <p><strong>Email:</strong> {patient.email}</p>
        <p><strong>Aadhaar ID:</strong> {patient.aadhaarId}</p>
      </div>

      {isProfileIncomplete && (
        <Button variant="contained" color="primary" startIcon={<ManageAccountsIcon />} onClick={() => setIsEditing(true)}>
          Update Profile
        </Button>
      )}

      <Modal open={isEditing} onClose={() => setIsEditing(false)} aria-labelledby="edit-profile-modal">
        <Box sx={{ position: 'absolute', top: '50%', left: '50%', transform: 'translate(-50%, -50%)', bgcolor: 'background.paper', boxShadow: 24, borderRadius: '8px', width: '500px', maxWidth: '90%', padding: 3 }}>
          <Typography variant="h5" sx={{ fontWeight: '600', marginBottom: 2, textAlign:'center' }}>Edit Profile</Typography>

          {error && <Alert severity='error' className='success-message'>{error}</Alert>}
          {success && <Alert severity='success' className='error-message'>{success}</Alert>}
          {imagePreview && (
            <Box sx={{ marginBottom: 2, textAlign: 'center' }}>
              <img src={imagePreview} alt="Preview" style={{ width: '60%', maxHeight: '40vh', borderRadius: '8px', marginTop: '10px', border: '5px solid #2a3f54' }} />
            </Box>
          )}
          <label htmlFor="file-input" style={{ cursor: 'pointer', marginBottom: '10px' }}>
            <Button startIcon={<Image />} component="span" sx={{ color: 'black', width: '100%' }} className="file-upload">
              {imageFile ? "Change Profile Image" : "Choose Profile Image"}
            </Button>
          </label>
          <input
            type="file"
            id="file-input"
            accept="image/*"
            onChange={handleImageChange}
          />
          <TextField
            fullWidth
            label="Aadhaar ID"
            name="aadhaarId"
            type="number"
            value={updatedPatient.aadhaarId || ""}
            onChange={handleInputChange}
            margin="normal"
            variant="outlined"
            sx={{
              '& .MuiOutlinedInput-root': {
                borderRadius: '8px',
              },
            }}
          />

          <TextField
            fullWidth
            label="Alternate Mobile Number"
            type="number"
            name="alternateMobile"
            value={updatedPatient.alternateMobile || ""}
            onChange={handleInputChange}
            margin="normal"
            variant="outlined"
            sx={{
              '& .MuiOutlinedInput-root': {
                borderRadius: '8px',
              },
            }}
          />
          <Box sx={{ display: 'flex', justifyContent: 'flex-end', marginTop: 2 }}>
            <Button variant="contained" color="primary" onClick={handleSaveChanges} sx={{ marginRight: 1 }} loading={loading}>
              Save Changes
            </Button>
            <Button variant="outlined" onClick={() => setIsEditing(false)}>Cancel</Button>
          </Box>
        </Box>
      </Modal>
    </div>
  );
};

PatientDetailsCard.propTypes = {
  patient: PropTypes.shape({
    id: PropTypes.number,
    fullName: PropTypes.string.isRequired,
    dateOfBirth: PropTypes.string.isRequired,
    gender: PropTypes.string.isRequired,
    mobile: PropTypes.string.isRequired,
    alternateMobile: PropTypes.string,
    email: PropTypes.string.isRequired,
    aadhaarId: PropTypes.string,
    image: PropTypes.string,
    address: PropTypes.object,
  }).isRequired,
  refreshProfileData: PropTypes.func.isRequired
};

export default PatientDetailsCard;
