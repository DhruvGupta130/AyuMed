import { useEffect, useState } from "react";
import { NavLink } from "react-router-dom";
import DashboardOutlinedIcon from "@mui/icons-material/DashboardOutlined";
import SettingsOutlinedIcon from "@mui/icons-material/SettingsOutlined";
import ExitToAppOutlinedIcon from "@mui/icons-material/ExitToAppOutlined";
import NotificationsOutlinedIcon from "@mui/icons-material/NotificationsOutlined";
// Icon for lab results
import { GroupsOutlined, MedicalInformationOutlined, MedicalServicesOutlined } from "@mui/icons-material";

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
                <NavLink to="/pharmacy/profile" className="sidebar-NavLink" onClick={handleNavLinkClick}>
                  <MedicalInformationOutlined className="sidebar-icon" />
                  Pharmacy Details
                </NavLink>
              </li>
              <li>
                <NavLink to="/pharmacy/pharmacist/profile" className="sidebar-NavLink" onClick={handleNavLinkClick}>
                  <DashboardOutlinedIcon className="sidebar-icon" />
                  Profile Details
                </NavLink>
              </li>
              <li>
                <NavLink to="/pharmacy/medications" className="sidebar-NavLink" onClick={handleNavLinkClick}>
                  <MedicalServicesOutlined className="sidebar-icon"/>
                  Medications
                </NavLink>
              </li>
              <li>
                <NavLink to="/pharmacy/patients" className="sidebar-NavLink" onClick={handleNavLinkClick}>
                  <GroupsOutlined className="sidebar-icon"/>
                  Orders
                </NavLink>
              </li>
              <li>
                <NavLink to="/pharmacy/notifications" className="sidebar-NavLink" onClick={handleNavLinkClick}>
                  <NotificationsOutlinedIcon className="sidebar-icon" />
                  Notifications
                </NavLink>
              </li>
              <li>
                <NavLink to="/pharmacy/settings" className="sidebar-NavLink" onClick={handleNavLinkClick}>
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
