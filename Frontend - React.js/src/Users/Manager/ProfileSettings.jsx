import { useState, useEffect } from "react";
import { TextField, Typography, Button, Alert, Box } from "@mui/material";
import { CheckCircleOutlineOutlined } from "@mui/icons-material";
import axios from "axios";
import { securedURL } from "../../Api & Services/Api.js";

const ProfileSettings = () => {
  const token = localStorage.getItem("token");
  const [formData, setFormData] = useState({
    oldPassword: "",
    newPassword: "",
    confirmNewPassword: "",
  });

  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (error) {
      const errorTimeout = setTimeout(() => setError(""), 3000);
      return () => clearTimeout(errorTimeout);
    }
  }, [error]);

  useEffect(() => {
    if (success) {
      const successTimeout = setTimeout(() => setSuccess(""), 3000);
      return () => clearTimeout(successTimeout);
    }
  }, [success]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const validatePasswords = () => {
    const { oldPassword, newPassword, confirmNewPassword } = formData;
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
    if (validationError) {
      setError(validationError);
      return;
    }

    setLoading(true);
    try {
      const response = await axios.put(`${securedURL}/update/password`, formData, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setSuccess(response.data.message || "Password updated successfully!");
      setFormData({ oldPassword: "", newPassword: "", confirmNewPassword: "" });
    } catch (error) {
      setError(error.response?.data?.message || "Error updating password!");
      console.error("Error updating password:", error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box sx={styles.container}>
      <Box sx={styles.card}>
        <Typography variant="h5" sx={styles.title}>
          🔒 Change Password
        </Typography>

        {error && <Alert severity="error" sx={styles.alert}>{error}</Alert>}
        {success && <Alert severity="success" sx={styles.alert}>{success}</Alert>}

        <form onSubmit={handlePasswordUpdate}>
          {/* Current Password */}
          <TextField
            fullWidth
            label="Current Password"
            name="oldPassword"
            type="password"
            value={formData.oldPassword}
            onChange={handleChange}
            margin="normal"
            variant="outlined"
            required
          />

          {/* New Password */}
          <TextField
            fullWidth
            label="New Password"
            name="newPassword"
            type="password"
            value={formData.newPassword}
            onChange={handleChange}
            margin="normal"
            variant="outlined"
            required
            helperText="Must include uppercase, lowercase, number & special character"
          />

          {/* Confirm New Password */}
          <TextField
            fullWidth
            label="Confirm New Password"
            name="confirmNewPassword"
            type="password"
            value={formData.confirmNewPassword}
            onChange={handleChange}
            margin="normal"
            variant="outlined"
            required
          />

          {/* Update Button */}
          <Button
            variant="contained"
            color="secondary"
            type="submit"
            endIcon={<CheckCircleOutlineOutlined/>}
            fullWidth
            sx={styles.button}
            loading={loading}
          >
            {loading ? "Updating..." : "Update Password"}
          </Button>
        </form>
      </Box>
    </Box>
  );
};

// Styles
const styles = {
  container: {
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    height: "80vh",
    padding: 2,
    width: '100%',
    backgroundColor: "#eef2f6",
  },
  card: {
    width: '500px',
    padding: "30px",
    borderRadius: "10px",
    boxShadow: "0 5px 15px rgba(0,0,0,0.1)",
    backgroundColor: "#fff",
    textAlign: "center",
  },
  title: {
    fontWeight: 600,
    marginBottom: "20px",
    color: "#333",
  },
  alert: {
    marginBottom: "15px",
  },
  button: {
    mt: 2,
    fontWeight: "bold",
    textTransform: "none",
    fontSize: "16px",
  },
};

export default ProfileSettings;
