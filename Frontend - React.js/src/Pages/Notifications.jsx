import { Alert } from "@mui/material";

const Notifications = () => {
    return(
        <div className="profile-container info-message"><Alert severity="info" icon={false} sx={{width: 'fit-content'}}> No New Notification</Alert></div>
    );
}

export default Notifications;