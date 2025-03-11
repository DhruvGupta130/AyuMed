import { useEffect, useState } from "react";
import { NavLink } from "react-router-dom";
import DashboardOutlinedIcon from "@mui/icons-material/DashboardOutlined";
import SettingsOutlinedIcon from "@mui/icons-material/SettingsOutlined";
import ExitToAppOutlinedIcon from "@mui/icons-material/ExitToAppOutlined";
import LocalHospitalOutlinedIcon from "@mui/icons-material/LocalHospitalOutlined";
import { Feedback, Group, Groups2 } from "@mui/icons-material";

const Sidebar = () => {
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);
  const toggleSidebar = () => {
    setIsSidebarOpen(!isSidebarOpen);
  };

  const closeSidebar = (e) => {
    if (!e.target.closest('.sidebar') && !e.target.closest('.sidebar-toggle')) {
      setIsSidebarOpen(false);
    }
  };

  const handleNavLinkClick = () => {
    setIsSidebarOpen(false);
  };

  useEffect(() => {
    document.addEventListener("mousedown", closeSidebar);
    return () => {
      document.removeEventListener("mousedown", closeSidebar);
    };
  }, []);

  return (
      <>
        <div className={`sidebar ${isSidebarOpen ? "show" : "hide"}`}>
          <div className="sidebar-wrapper">
            <ul className="sidebar-menu">
              <li>
                <NavLink to="/hospital/profile" className="sidebar-NavLink" onClick={handleNavLinkClick}>
                  <LocalHospitalOutlinedIcon className="sidebar-icon" />
                  Hospital Details
                </NavLink>
              </li>
              <li>
                <NavLink to="/hospital/manager/profile" className="sidebar-NavLink" onClick={handleNavLinkClick}>
                  <DashboardOutlinedIcon className="sidebar-icon" />
                  Profile Details
                </NavLink>
              </li>
              <li>
                <NavLink to="/hospital/doctors" className="sidebar-NavLink" onClick={handleNavLinkClick}>
                  <Group className="sidebar-icon" />
                  Doctors
                </NavLink>
              </li>
              <li>
                <NavLink to="/hospital/patients" className="sidebar-NavLink" onClick={handleNavLinkClick}>
                  <Groups2 className="sidebar-icon" />
                  Patients
                </NavLink>
              </li>
              <li>
                <NavLink to="/hospital/feedbacks" className="sidebar-NavLink" onClick={handleNavLinkClick}>
                  <Feedback className="sidebar-icon" />
                  Feedback & Reviews
                </NavLink>
              </li>
              <li>
                <NavLink to="/hospital/settings" className="sidebar-NavLink" onClick={handleNavLinkClick}>
                  <SettingsOutlinedIcon className="sidebar-icon" />
                  Profile Settings
                </NavLink>
              </li>
              <li>
                <NavLink to="/logout" className="sidebar-NavLink" onClick={handleNavLinkClick}>
                  <ExitToAppOutlinedIcon className="sidebar-icon" />
                  Logout
                </NavLink>
              </li>
            </ul>
          </div>
        </div>

        {/* Hamburger Menu */}
        <div className="sidebar-toggle" onClick={toggleSidebar}>
          &#9776; {/* Hamburger icon */}
        </div>
      </>
  );
};

export default Sidebar;
