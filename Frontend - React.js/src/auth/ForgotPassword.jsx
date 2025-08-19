import {useEffect, useState} from 'react';
import {message} from 'antd';
import {Link, useNavigate} from 'react-router-dom';
import {Alert, Box, CircularProgress} from '@mui/material';
import '../Styles/Auth.css';
import axios from "axios";
import {BACKEND_URL} from "../configuration.js";

function ForgotPassword() {
    const [email, setEmail] = useState('');
    const [success, setSuccess] = useState('');  
    const [otp, setOtp] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [loading, setLoading] = useState(false);
    const [step, setStep] = useState(1);
    const [resendTimer, setResendTimer] = useState(0);
    const navigate = useNavigate();

    useEffect(() => {
        if (resendTimer > 0) {
            const timer = setTimeout(() => setResendTimer(resendTimer - 1), 1000);
            return () => clearTimeout(timer);
        }
    }, [resendTimer]);

    useEffect(() => {
        setTimeout(() => {
            setSuccess('');
        }, 8000);
    }, [success]);

    const handleForgotPassword = async (e) => {
        e.preventDefault();
    
        if (step === 1) {
            // Email validation: Check if email is properly formatted
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!email || !emailRegex.test(email)) {
                message.error("Please enter a valid email address.");
                return;
            }
    
            setLoading(true);
            try {
                const response = await axios.post(`${BACKEND_URL}/forgot-password?email=${email}`);
                message.success(response.data?.message || "OTP has been sent!");
                setStep(2);
                setResendTimer(30); // Start 30s countdown
            } catch (error) {
                message.error(error.response?.data?.message || "An error occurred. Please try again.");
                console.error(error);
            } finally {
                setLoading(false);
            }
        } else if (step === 2) {
            // OTP validation: Ensure exactly 6 numeric digits
            if (!otp || !/^\d{6}$/.test(otp)) {
                message.error("Please enter a valid 6-digit OTP.");
                return;
            }
    
            // Password validation: Must meet strong password criteria
            const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
            if (!newPassword || !passwordRegex.test(newPassword)) {
                message.error("Password must be at least 8 characters and include uppercase, lowercase, number, and special character.");
                return;
            }
    
            // Confirm Password validation
            if (!confirmPassword) {
                message.error("Please confirm your password.");
                return;
            }
    
            if (newPassword !== confirmPassword) {
                message.error("Passwords do not match!");
                return;
            }
    
            setLoading(true);
            try {
                const response = await axios.put(`${BACKEND_URL}/update/password?email=${email}&otp=${otp}&password=${newPassword}`);
                message.success(response.data?.message || "Password updated successfully!");
                navigate("/login");
            } catch (error) {
                message.error(error.response?.data?.message || "Invalid OTP or error occurred.");
                console.error(error);
            } finally {
                setLoading(false);
            }
        }
    };
    

    const handleResendOTP = async () => {
        if (!email) {
            message.error("Please enter your email address.");
            return;
        }
        setLoading(true);
        try {
            const response = await axios.post(`${BACKEND_URL}/forgot-password?email=${email}`);
            message.success(response.data?.message || "OTP has been resent!");
            setResendTimer(30); // Restart 30s countdown
        } catch (error) {
            message.error(error.response?.data?.message || "An error occurred. Please try again.");
            console.error(error);
        } finally {
            setLoading(false);
        }
    };

    const handleGetUsername = async () => {
        if (!email) {
            message.error("Please enter your email address.");
            return;
        }
        setLoading(true);
        try {
            const response = await axios.get(`${BACKEND_URL}/get-username?email=${email}`);
            setSuccess(response.data?.message || "No user found with this email.");
        } catch (error) {
            message.error(error.response?.data?.message || "An error occurred. Please try again.");
            console.error(error);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="auth-container">
            <div className="auth-box">
                <h2 className="auth-title">
                    <span>{step === 1 ? "Account Assistance" : "Verify OTP and Set New Password"}</span>
                </h2>
                {loading ? (
                    <Box display="flex" justifyContent="center" alignItems="center" height="100px">
                        <CircularProgress size={40} color="primary" />
                    </Box>
                ) : (
                    <form onSubmit={handleForgotPassword}>
                        {success && <Alert style={{ marginBottom: 10 }}>{success}</Alert>}
                        {step === 1 ? (
                            <div className="input-field">
                                <label htmlFor="email">Email</label>
                                <input
                                    type="email"
                                    id="email"
                                    placeholder="Enter your email"
                                    value={email}
                                    onChange={(e) => setEmail(e.target.value)}
                                    required
                                />
                            </div>
                        ) : (
                            <>
                                <div className="input-field">
                                    <label htmlFor="otp">OTP</label>
                                    <input
                                        type="text"
                                        id="otp"
                                        placeholder="Enter OTP"
                                        value={otp}
                                        onChange={(e) => setOtp(e.target.value)}
                                        required
                                    />
                                    {/* Resend OTP Small Highlighted Text */}
                                    <p className="resend-otp-text" onClick={resendTimer === 0 ? handleResendOTP : null} style={{ 
                                        color: resendTimer === 0 ? "#007bff" : "#6c757d", 
                                        cursor: resendTimer === 0 ? "pointer" : "default",
                                        fontSize: "12px",
                                        marginTop: "5px"
                                    }}>
                                        {resendTimer > 0 ? `Resend OTP in ${resendTimer}s` : "Resend OTP"}
                                    </p>
                                </div>
                                <div className="input-field">
                                    <label htmlFor="newPassword">New Password</label>
                                    <input
                                        type="password"
                                        id="newPassword"
                                        placeholder="Enter your new password"
                                        value={newPassword}
                                        onChange={(e) => setNewPassword(e.target.value)}
                                        required
                                    />
                                    <label htmlFor="confirmPassword" style={{marginTop: 10}}>Confirm Password</label>
                                    <input
                                        type="password"
                                        id="confirmPassword"
                                        placeholder="Re-enter your password"
                                        value={confirmPassword}
                                        onChange={(e) => setConfirmPassword(e.target.value)}
                                        required
                                    />
                                </div>
                            </>
                        )}
                        <div style={{ display: "flex", gap: 20, justifyContent: "space-between" }}>
                            {step === 1 && <button type="button" className="auth-btn" onClick={handleGetUsername}>Get Username</button>}
                            <button type="submit" className="auth-btn">
                                {step === 1 ? "Reset Password" : "Update Password"}
                            </button>
                        </div>
                    </form>
                )}

                <div className="auth-footer">
                    <p>Remember your password? <Link to="/login">Login</Link></p>
                </div>
            </div>
        </div>
    );
}

export default ForgotPassword;
