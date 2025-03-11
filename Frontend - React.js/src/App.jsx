import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Navbar from "./Components/Navbar";
import Home from "./Pages/Home";
import Login from "./auth/Login.jsx";
import Register from "./auth/Register.jsx";
import ServicesPage from "./Pages/ServicesPage.jsx";
import AnalyticsDashboard from "./Components/AnalyticsDashboard.jsx";
import HospitalListPage from "./Pages/HospitalListPage.jsx";
import Contact from "./Pages/Contact.jsx";
import Logout from "./Components/Logout.jsx";
import NotFound from "./Pages/NotFound.jsx";
import CustomRoutesManager from "./Users/Manager/CustomRoutesManager.jsx";
import CustomRoutesDoctor from "./Users/Doctor/CustomRoutesDoctor.jsx";
import CustomRoutesPharmacy from "./Users/Pharmacist/CustomRoutesPharmacy.jsx";
import PrivateRoute from "./auth/PrivateRoute.jsx";
import NotAuthorized from "./Pages/NotAuthorized.jsx";
import CustomRoutesPatient from "./Users/Patient/CustomRoutesPatient.jsx";
import CustomRoutesPages from "./Pages/CustomPages/CustomRoutesPages.jsx";
import ForgotPassword from "./auth/ForgotPassword.jsx";
import SearchPage from "./Pages/SearchPage.jsx";
import "./Styles/Sidebar.css";
import PharmacyListPage from "./Pages/PharmacyListPage.jsx";


const App = () => {
    return (
        <Router>
            <Navbar />
            <Routes>
                {/* Public Routes */}
                <Route path="/" element={<Home />} />
                <Route path="/search" element={<SearchPage />} />
                <Route path="/login" element={<Login />} />
                <Route path="/forgot-password" element={<ForgotPassword />} />
                <Route path="/register" element={<Register />} />
                <Route path="/services" element={<ServicesPage />} />
                <Route path="/dashboard" element={<AnalyticsDashboard />} />
                <Route path="/nearby-hospitals" element={<HospitalListPage />} />
                <Route path="/nearby-pharmacies" element={<PharmacyListPage />} />
                <Route path="/contact" element={<Contact />} />
                <Route path="/logout" element={<Logout />} />
                <Route path="/not-authorized" element={<NotAuthorized />} />
                
                {/* Protected Routes */}
                <Route
                    path="/patient/*"
                    element={
                        <PrivateRoute roles={['ROLE_PATIENT']}>
                            <CustomRoutesPatient />
                        </PrivateRoute>
                    }
                />

                <Route 
                    path="/page/*" 
                    element={
                        <PrivateRoute roles={['ROLE_PATIENT']}>
                                <CustomRoutesPages />
                        </PrivateRoute>
                    }   
                />

                <Route
                    path="/patient/*"
                    element={
                        <PrivateRoute roles={['ROLE_PATIENT']}>
                            <CustomRoutesPatient />
                        </PrivateRoute>
                    }
                />

                <Route
                    path="/hospital/*"
                    element={
                        <PrivateRoute roles={['ROLE_MANAGEMENT']}>
                            <CustomRoutesManager />
                        </PrivateRoute>
                    }
                />
                <Route
                    path="/pharmacy/*"
                    element={
                        <PrivateRoute roles={['ROLE_PHARMACIST']}>
                            <CustomRoutesPharmacy />
                        </PrivateRoute>
                    }
                />
                <Route
                    path="/doctor/*"
                    element={
                        <PrivateRoute roles={['ROLE_DOCTOR']}>
                            <CustomRoutesDoctor />
                        </PrivateRoute>
                    }
                />
                <Route path="*" element={<NotFound />} />
            </Routes>
        </Router>
    );
};

export default App;
