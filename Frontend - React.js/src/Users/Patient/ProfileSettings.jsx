import { useEffect, useState } from 'react';
import {
  TextField,
  Typography,
  Button,
  Alert,

} from '@mui/material';
import axios from 'axios';
import { CheckCircleOutline } from '@mui/icons-material';
import { securedURL } from "../../Api & Services/Api.js";
import { useNavigate } from 'react-router-dom';

const ProfileSettings = () => {
  
  const token = localStorage.getItem('token');
  useNavigate();
  const [passwords, setPasswords] = useState({
    oldPassword: '',
    newPassword: '',
    confirmNewPassword: '',
  });

  // State for delete account dialog
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (error) {
      const errorTimeout = setTimeout(() => {
        setError('');
      }, 3000);
      return () => clearTimeout(errorTimeout);
    }
  }, [error]);

  useEffect(() => {
    if (success) {
      const successTimeout = setTimeout(() => {
        setSuccess('');
      }, 3000);
      return () => clearTimeout(successTimeout);
    }
  }, [success]);

  const handlePasswordChange = (e) => {
    const { name, value } = e.target;
    setPasswords({ ...passwords, [name]: value });
  };

  const validatePasswords = () => {
    const { oldPassword, newPassword, confirmNewPassword } = passwords;
    if (!oldPassword || !newPassword) return "Password fields cannot be empty!";
    if (newPassword.length < 8)
      return "Password must be at least 8 characters long!";
    if (!/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])/.test(newPassword))
      return "Password must include uppercase, lowercase, number & special character!";
    if (newPassword !== confirmNewPassword) return "Passwords do not match!";
    return null;
  };

  const handlePasswordUpdate = async (e) => {
    e.preventDefault();
    const validationError = validatePasswords();
    setLoading(true);
    if (validationError) {
      setError(validationError);
      return;
    }

    try {
      const response = await axios.put(`${securedURL}/update/password`, passwords, {
        "headers":{
          "Authorization": `Bearer ${token}`
        }
      });
      setSuccess(response.data || 'Password updated successfully!');
    } catch (error) {
      if (error.response?.data?.message) {
        setError(error.response.data.message || "Error updating Password");
        console.error("Error update password: ", error);
      } else {
        setError('Error updating password!');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className='profile-settings-container' style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '80vh'}}>
      <div>
        {/* Password Update Section */}
        <div className='update-password' style={{maxWidth: '500px'}}>
          <Typography variant="h5">🔒 Change Password</Typography>
          {error && <Alert severity="error" sx={{marginY: "15px"}} >{error}</Alert>}
          {success && <Alert severity="success" sx={{marginY: "15px"}}>{success}</Alert>}
          <form onSubmit={handlePasswordUpdate}>
            <TextField
              fullWidth
              label="Current Password"
              name="oldPassword"
              type="password"
              value={passwords.oldPassword}
              onChange={handlePasswordChange}
              margin="normal"
              autoComplete="off"
              required
            />
            <TextField
              fullWidth
              label="New Password"
              name="newPassword"
              type="password"
              value={passwords.newPassword}
              onChange={handlePasswordChange}
              margin="normal"
              autoComplete="off"
              required
              helperText="Must include uppercase, lowercase, number & special character"
            />
            <TextField
              fullWidth
              label="Confirm New Password"
              name="confirmNewPassword"
              type="password"
              value={passwords.confirmNewPassword}
              onChange={handlePasswordChange}
              margin="normal"
              required
              autoComplete="off"
            />
            <Button
              variant="contained"
              color="secondary"
              type="submit"
              fullWidth
              endIcon={<CheckCircleOutline/>}
              sx={{
                mt: 2,
                fontWeight: "bold",
                textTransform: "none",
                fontSize: "16px",
              }}
              loading={loading}
            >
              {loading ? "Updating..." : "Update Password"}
            </Button>
          </form>
        </div>
      </div>
    </div>
  );
};

export default ProfileSettings;
