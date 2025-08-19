import {useEffect, useState} from "react";
import {Link, useNavigate} from "react-router-dom";
import axios from 'axios';
import "../Styles/Auth.css";
import {message} from "antd";
import {Box, CircularProgress} from "@mui/material";
import {BACKEND_URL} from "../configuration.js";


function Register() {
  const navigate = useNavigate();
  
  const [formData, setFormData] = useState({
    username: "",
    password: "",
    confirmPassword: "",
    role: "", 
    firstName: "",
    lastName: "",
    gender: "", 
    email: "",
    mobile: "",
    dateOfBirth: "",
  });
  const [loading, setLoading] = useState(false);
  const [otp, setOtp] = useState("");
  const [otpSent, setOtpSent] = useState(false);
  const [verificationLoading, setVerificationLoading] = useState(false);
  const [resendTimer, setResendTimer] = useState(0);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleOtpChange = (e) => {
    setOtp(e.target.value);
  };

  const validatePasswords = () => {
    const { password, confirmPassword } = formData;
    if (!password) return "Password fields cannot be empty!";
    if (password.length < 8)
      return "Password must be at least 8 characters long!";
    if (!/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])/.test(password))
      return "Password must include uppercase, lowercase, number & special character!";
    if (password !== confirmPassword) return "Passwords do not match!";
    return null;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const validationError = validatePasswords();
    if (validationError) {
      message.error(validationError);
      return;
    }

    if(formData.mobile.length!==10){
      message.error("Invalid Mobile Number!");
      return;
    }

    setLoading(true);

    try {
      const otpResponse = await axios.post(`${BACKEND_URL}/otp/send`, { email: formData.email });
      message.success(otpResponse.data?.message);
      setOtpSent(true);
      setResendTimer(30);
    } catch (error) {
        message.error(error?.response?.data?.message || "Error sending OTP");
        console.error(error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
      if (resendTimer > 0) {
          const timer = setTimeout(() => setResendTimer(resendTimer - 1), 1000);
          return () => clearTimeout(timer);
      }
  }, [resendTimer]);

  const handleVerifyOtp = async (e) => {
    e.preventDefault();
    setVerificationLoading(true);
    try {
      const userData = {
        username: formData.username,
        password: formData.password,
        role: formData.role,
        firstName: formData.firstName,
        lastName: formData.lastName,
        gender: formData.gender,
        email: formData.email,
        mobile: formData.mobile,
        dateOfBirth: formData.dateOfBirth,
        otp: otp
      };
      await axios.post(`${BACKEND_URL}/register`, userData);
      message.success("User registered successfully");
      navigate("/login");
    } catch (error) {
      message.error(error?.response?.data?.message || "Error verifying OTP");
      console.error(error);
    } finally {
      setVerificationLoading(false);
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-box">
        <h2 className="auth-title">
          <span>Register</span>
        </h2>
        {loading ? (
          <Box display="flex" justifyContent="center" alignItems="center" height="100px">
            <CircularProgress size={40} color="primary" />
          </Box>
        ) : !otpSent ? (
          <form onSubmit={handleSubmit}>
            {/* Role Selector */}
            <div className="input-field">
              <label htmlFor="role">Select Role</label>
              <select
                id="role"
                name="role"
                value={formData.role}
                onChange={handleChange}
                required
              >
                <option value="" disabled >Select Role</option>
                <option value="ROLE_PATIENT">Patient</option>
                <option value="ROLE_MANAGEMENT">Hospital Manager</option>
                <option value="ROLE_PHARMACIST">Pharmacist</option>
              </select>
            </div>

            {/* Username Field */}
            <div className="input-field">
              <label htmlFor="username">Username</label>
              <input
                type="text"
                id="username"
                name="username"
                value={formData.username}
                onChange={handleChange}
                required
              />
            </div>

            {/* Password Field */}
            <div className="input-field">
              <label htmlFor="password">Password</label>
              <input
                type="password"
                id="password"
                name="password"
                value={formData.password}
                onChange={handleChange}
                required
              />
              <small>Must be 8+ characters, include uppercase, lowercase, number & special character.</small>
            </div>

            {/* Confirm Password Field */}
            <div className="input-field">
              <label htmlFor="confirmPassword">Confirm Password</label>
              <input
                type="password"
                id="confirmPassword"
                name="confirmPassword"
                value={formData.confirmPassword}
                onChange={handleChange}
                required
              />
            </div>

            {/* First Name Field */}
            <div className="input-field">
              <label htmlFor="firstName">First Name</label>
              <input
                type="text"
                id="firstName"
                name="firstName"
                value={formData.firstName}
                onChange={handleChange}
                required
              />
            </div>

            {/* Last Name Field */}
            <div className="input-field">
              <label htmlFor="lastName">Last Name</label>
              <input
                type="text"
                id="lastName"
                name="lastName"
                value={formData.lastName}
                onChange={handleChange}
                required
              />
            </div>

            {/* Gender Field */}
            <div className="input-field">
              <label htmlFor="gender">Gender</label>
              <select
                id="gender"
                name="gender"
                value={formData.gender}
                onChange={handleChange}
                required
              >
                <option value="" disabled >Select Gender</option>
                <option value="MALE">Male</option>
                <option value="FEMALE">Female</option>
                <option value="OTHER">Other</option>
              </select>
            </div>

            {/* Email Field */}
            <div className="input-field">
              <label htmlFor="email">Email</label>
              <input
                type="email"
                id="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                required
              />
            </div>

            {/* Mobile Field */}
            <div className="input-field">
              <label htmlFor="mobile">Mobile</label>
              <input
                type="number"
                id="mobile"
                name="mobile"
                value={formData.mobile}
                onChange={handleChange}
                required
              />
            </div>

            {/* Conditional Fields for Patient */}
            {formData.role === "ROLE_PATIENT" && (
              <div className="input-field">
                <label htmlFor="dateOfBirth">Date of Birth</label>
                <input
                  type="date"
                  id="dateOfBirth"
                  name="dateOfBirth"
                  value={formData.dateOfBirth}
                  onChange={handleChange}
                  required
                />
              </div>
            )}

            {/* Submit Button */}
            <button type="submit" className="auth-btn">Send OTP</button>
          </form>) : (
            <form onSubmit={handleVerifyOtp}>
              <div className="input-field">
                <label htmlFor="otp">Enter OTP</label>
                <input
                    type="text"
                    id="otp"
                    name="otp"
                    value={otp}
                    onChange={handleOtpChange}
                    required
                />
                {/* Resend OTP Small Highlighted Text */}
                <p className="resend-otp-text" onClick={resendTimer === 0 ? handleSubmit : null} style={{ 
                    color: resendTimer === 0 ? "#007bff" : "#6c757d", 
                    cursor: resendTimer === 0 ? "pointer" : "default",
                    fontSize: "12px",
                    marginTop: "5px"
                }}>
                    {resendTimer > 0 ? `Resend OTP in ${resendTimer}s` : "Resend OTP"}
                </p>
              </div>
              <button type="submit" className="auth-btn" disabled={verificationLoading}>
                {verificationLoading ? "Verifying..." : "Verify OTP"}
              </button>
            </form>
        )}

        <div className="auth-footer">
          <p>Already have an account? <Link to="/Login">Login</Link></p>
        </div>
      </div>
    </div>
  );
}

export default Register;
