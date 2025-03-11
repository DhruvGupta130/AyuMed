import { useEffect, useState } from "react";
import { NavLink } from "react-router-dom";
import DashboardOutlinedIcon from "@mui/icons-material/DashboardOutlined";
import EventNoteOutlinedIcon from "@mui/icons-material/EventNoteOutlined";
import SettingsOutlinedIcon from "@mui/icons-material/SettingsOutlined";
import ExitToAppOutlinedIcon from "@mui/icons-material/ExitToAppOutlined";
import "@mui/icons-material/DocumentScannerOutlined";
import NotificationsOutlinedIcon from "@mui/icons-material/NotificationsOutlined";
import { FeedbackOutlined, Groups2Outlined, Schedule } from "@mui/icons-material";

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

  // Close sidebar when clicking on a menu option
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
                <NavLink to="/doctor/profile" className="sidebar-NavLink" onClick={handleNavLinkClick}>
                  <DashboardOutlinedIcon className="sidebar-icon" />
                  Profile Details
                </NavLink>
              </li>
              <li>
                <NavLink to="/doctor/appointments" className="sidebar-NavLink" onClick={handleNavLinkClick}>
                  <EventNoteOutlinedIcon className="sidebar-icon" />
                  Appointments
                </NavLink>
              </li>
              <li>
                <NavLink to="/doctor/schedule" className="sidebar-NavLink" onClick={handleNavLinkClick}>
                  <Schedule className="sidebar-icon" />
                  Schedule
                </NavLink>
              </li>
              <li>
                <NavLink to="/doctor/patients" className="sidebar-NavLink" onClick={handleNavLinkClick}>
                  <Groups2Outlined className="sidebar-icon" />
                  Patients
                </NavLink>
              </li>
              <li>
                <NavLink to="/doctor/feedbacks" className="sidebar-NavLink" onClick={handleNavLinkClick}>
                <FeedbackOutlined className="sidebar-icon" />
                  Feedbacks
                </NavLink>
              </li>
              <li>
                <NavLink to="/doctor/notifications" className="sidebar-NavLink" onClick={handleNavLinkClick}>
                  <NotificationsOutlinedIcon className="sidebar-icon" />
                  Notifications
                </NavLink>
              </li>
              <li>
                <NavLink to="/doctor/settings" className="sidebar-NavLink" onClick={handleNavLinkClick}>
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
