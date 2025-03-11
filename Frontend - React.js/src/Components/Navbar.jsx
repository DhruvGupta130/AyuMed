import { useCallback, useEffect, useState } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faXmark, faSearch, faBars } from "@fortawesome/free-solid-svg-icons";
import "../Styles/Navbar.css";
import { Link, useNavigate } from "react-router-dom";
import { Avatar } from "@mui/material";
import { red } from "@mui/material/colors";
import { IMAGE_URL } from "../Api & Services/Api.js";
import { fetchManagerProfileData } from "../Users/Manager/fetchManagerProfileData.jsx";
import { fetchDoctorProfileData } from "../Users/Doctor/fetchDoctorProfileData.jsx";
import { fetchPharmacistProfileData } from "../Users/Pharmacist/fetchPharmacistProfileData.jsx";
import { fetchPatientProfileData } from "../Users/Patient/fetchPatientProfileData.jsx";
import { getAvatarText } from "../Api & Services/Services.js";

function Navbar() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [searchTerm, setSearchTerm] = useState("");
  const [isNavbarOpen, setIsNavbarOpen] = useState(false);
  const [user, setUser] = useState({
    image: "",
    fullName: "",
  });
  const navigate = useNavigate();
  const isAuthenticated = localStorage.getItem("isAuthenticated");
  const role = localStorage.getItem('role');
  const [profile, setProfile] = useState("");

  useEffect(() => {
    if (isAuthenticated) {
      if (role === "ROLE_PATIENT") setProfile("/patient/profile");
      else if (role === "ROLE_DOCTOR") setProfile("/doctor/profile");
      else if (role === "ROLE_MANAGEMENT") setProfile("/hospital/profile");
      else if (role === "ROLE_PHARMACIST") setProfile("/pharmacy/profile");
    }
  }, [isAuthenticated, role]);

  useEffect(() => {
    setIsLoggedIn(isAuthenticated === "true");
  }, [isAuthenticated]);

  const fetchProfile = useCallback(() => {
    if (role === "ROLE_PATIENT") {
      fetchPatientProfileData(setUser, () => {}, () => {});
    } else if (role === "ROLE_MANAGEMENT") {
      fetchManagerProfileData(setUser, () => {}, () => {});
    } else if (role === "ROLE_DOCTOR") {
      fetchDoctorProfileData(setUser, () => {}, () => {});
    } else if (role === "ROLE_PHARMACIST") {
      fetchPharmacistProfileData(setUser, () => {}, () => {});
    }
  }, [role]);

  useEffect(() => {
    if (isLoggedIn) {
      fetchProfile();
    }
  }, [fetchProfile, isLoggedIn]);

  const handleSearch = (e) => {
    e.preventDefault();
    console.log("Searching for:", searchTerm);
    navigate(`/search?keyword=${searchTerm}`);
    setSearchTerm("");
    closeNavbar();
  };

  const handleLoginClick = () => {
    if (!isLoggedIn) {
      navigate("/login");
    } else {
      navigate("/profile");
    }
  };

  const toggleNavbar = () => {
    setIsNavbarOpen(!isNavbarOpen);
  };

  const closeNavbar = () => {
    setIsNavbarOpen(false);
  };

  return (
    <div className="navbar-section">
      <h1 className="navbar-title">
        <Link to="/">
          Ayu<span className="navbar-sign">Med</span>
        </Link>
      </h1>

      {/* Desktop Navbar */}
      <ul className="navbar-items">
        <li>
          <Link to="/" className="navbar-links">
            Home
          </Link>
        </li>
        <li>
          <Link to="/services" className="navbar-links">
            Services
          </Link>
        </li>
        <li>
          <Link to="/dashboard" className="navbar-links">
            Dashboard
          </Link>
        </li>
        <li>
          <Link to="/contact" className="navbar-links">
            Contact us
          </Link>
        </li>
        <li>
          <form onSubmit={handleSearch} className="search-form">
            <input
              type="text"
              className="search-input"
              placeholder="Search..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
            <button type="submit" className="search-btn">
              <FontAwesomeIcon icon={faSearch} className="search-icon" />
            </button>
          </form>
        </li>
        <li>
          {isLoggedIn ? (
            <Link to={profile} className="avatar-link">
              {user.image ? (
                <img
                  src={`${IMAGE_URL}${user.image}`}
                  alt={user.fullName}
                  className="avatar-img"
                />
              ) : (
                <Avatar sx={{ bgcolor: red[700], width: 50, height: 50 }}>
                  {getAvatarText(user.fullName)}
                </Avatar>
              )}
            </Link>
          ) : (
            <button onClick={handleLoginClick} className="navbar-btn">
              Login
            </button>
          )}
        </li>
      </ul>

      {/* Mobile Navbar */}
      <div className={`mobile-navbar ${isNavbarOpen ? "open-nav" : ""}`}>
        <button
          onClick={toggleNavbar}
          className="mobile-navbar-options"
          aria-label="Toggle Navigation"
        >
          {isNavbarOpen ? (
            <FontAwesomeIcon icon={faXmark} />
          ) : (
            <FontAwesomeIcon icon={faBars} />
          )}
        </button>
        <ul className="mobile-navbar-links">
          <li>
            {isLoggedIn ? (
              <Link to={profile} onClick={toggleNavbar} className="avatar-link">
                <div className="avatar">
                  {user.image ? (
                    <img
                      src={`${IMAGE_URL}${user.image}`}
                      alt={user.fullName}
                      className="avatar-img"
                    />
                  ) : (
                    <Avatar
                      sx={{ bgcolor: red[700], width: 80, height: 80 }}
                    >
                      {getAvatarText(user.fullName)}
                    </Avatar>
                  )}
                </div>
              </Link>
            ) : (
              <button onClick={handleLoginClick} className="navbar-btn">
                Login
              </button>
            )}
          </li>
          <li>
            <Link to="/" onClick={toggleNavbar}>
              Home
            </Link>
          </li>
          <li>
            <Link to="/services" onClick={toggleNavbar}>
              Services
            </Link>
          </li>
          <li>
            <Link to="/contact" onClick={toggleNavbar}>
              Contact us
            </Link>
          </li>
          <li>
            {isLoggedIn ? (
              <Link to="/logout" onClick={toggleNavbar}>
                Logout
              </Link>
            ) : (
              <Link to="/login" onClick={toggleNavbar}>
                Login
              </Link>
            )}
          </li>
          <li>
            <form onSubmit={handleSearch} className="search-form">
              <input
                type="text"
                className="search-input"
                placeholder="Search..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
              />
              <button type="submit" className="search-btn">
                <FontAwesomeIcon icon={faSearch} className="search-icon" />
              </button>
            </form>
          </li>
        </ul>
      </div>
    </div>
  );
}

export default Navbar;
