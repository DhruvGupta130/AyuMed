import { useEffect, useState } from "react";
import { NavLink } from "react-router-dom";
import DashboardOutlinedIcon from "@mui/icons-material/DashboardOutlined";
import EventNoteOutlinedIcon from "@mui/icons-material/EventNoteOutlined";
import SettingsOutlinedIcon from "@mui/icons-material/SettingsOutlined";
import ExitToAppOutlinedIcon from "@mui/icons-material/ExitToAppOutlined";
import DocumentScannerOutlinedIcon from "@mui/icons-material/DocumentScannerOutlined";
import NotificationsOutlinedIcon from "@mui/icons-material/NotificationsOutlined";
import LocalHospitalOutlinedIcon from "@mui/icons-material/LocalHospitalOutlined";
import { MedicationLiquidOutlined, ReceiptLongOutlined } from "@mui/icons-material";

const Sidebar = () => {
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);

  // Toggle sidebar visibility
  const toggleSidebar = () => {
    setIsSidebarOpen(!isSidebarOpen);
  };

  // Close sidebar when clicking outside of it
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
              <NavLink to="/patient/profile" className="sidebar-NavLink" onClick={handleNavLinkClick}>
                <DashboardOutlinedIcon className="sidebar-icon" />
                Profile Details
              </NavLink>
            </li>
            <li>
              <NavLink to="/patient/appointments" className="sidebar-NavLink" onClick={handleNavLinkClick}>
                <EventNoteOutlinedIcon className="sidebar-icon" />
                Appointments
              </NavLink>
            </li>
            <li>
              <NavLink to="/patient/documents" className="sidebar-NavLink" onClick={handleNavLinkClick}>
                <DocumentScannerOutlinedIcon className="sidebar-icon" />
                Documents
              </NavLink>
            </li>
            <li>
              <NavLink to="/patient/records" className="sidebar-NavLink" onClick={handleNavLinkClick}>
                <ReceiptLongOutlined className="sidebar-icon" />
                Medical Records
              </NavLink>
            </li>
            <li>
              <NavLink to="/patient/medications" className="sidebar-NavLink" onClick={handleNavLinkClick}>
                <MedicationLiquidOutlined className="sidebar-icon" />
                Medications
              </NavLink>
            </li>
            <li>
              <NavLink to="/patient/lab-results" className="sidebar-NavLink" onClick={handleNavLinkClick}>
                <LocalHospitalOutlinedIcon className="sidebar-icon" />
                Lab Results
              </NavLink>
            </li>
            <li>
              <NavLink to="/patient/notifications" className="sidebar-NavLink" onClick={handleNavLinkClick}>
                <NotificationsOutlinedIcon className="sidebar-icon" />
                Notifications
              </NavLink>
            </li>
            <li>
              <NavLink to="/patient/settings" className="sidebar-NavLink" onClick={handleNavLinkClick}>
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
