import { format } from "date-fns";
import { Avatar, Button, Chip, Dialog, DialogActions, DialogContent, DialogTitle, TextField } from "@mui/material";
import { red } from "@mui/material/colors";
import PropTypes from "prop-types";
import { patientURL } from "../../Api & Services/Api.js";
import axios from "axios";
import { useState } from "react";
import { message } from "antd";
import { getAvatarText } from "../../Api & Services/Services.js";
import { AccessTime, CancelPresentationOutlined, CheckCircleOutline, DoneAll, ErrorOutline, FeedbackOutlined, HourglassEmpty } from "@mui/icons-material";
import FeedbackModal from "./FeedbackModal.jsx";

const AppointmentCard = ({ appointment, refresh, setSuccess }) => {
  appointment.date = format(new Date(appointment.appointmentDate), "MMMM dd, yyyy");
  appointment.time = format(new Date(appointment.appointmentDate), "hh:mm a");

  const [appointmentData, setAppointmentData] = useState({
    id: appointment.id,
    cancellationReason: ''
  });

  const [openModal, setOpenModal] = useState(false);
  const [feedbackModal, setFeedbackModal] = useState(false);
  const [loading, setLoading] = useState(false);

  const handleAppointmentCancellation = async () => {
    if(!appointmentData.cancellationReason) {
      message.error("Enter cancellation reason");
      return;
    }
    setLoading(true);
    try{
      const token = localStorage.getItem('token');
      const response = await axios.put(`${patientURL}/appointment/cancel`, appointmentData,{
        headers:{
          Authorization: `Bearer ${token}`
        }
      });
      setSuccess(response.data.message);
      refresh();
      setTimeout(() => setSuccess(""), 2000);
    } catch (error) {
      message.error(error.response?.data?.message || "Error while cancelling appointment");
      console.error("Error canceling appointment:", error);
    }finally {
      setLoading(false);
    }
  }

  const handleOpenModal = () => setOpenModal(true);
  const handleCloseModal = () => setOpenModal(false);

  const openFeedbackModal = () => setFeedbackModal(true);
  const closeFeedbackModal = () => setFeedbackModal(false);
  
  const handleCancellationReasonChange = (e) => {
    setAppointmentData({
      ...appointmentData,
      cancellationReason: e.target.value
    });
  };

  return (
    <div className="upcoming-card">
      <div className="image-container">
        {appointment.doctorImage ? (
          <img
            src={appointment.doctorImage}
            alt={`Dr. ${appointment.doctorName}`}
            className="card-photo"
          />
        ) : (
          <Avatar className='card-photo' variant="rounded" sx={{ bgcolor: red[700], width: 120, height: 150}}>{getAvatarText(appointment.doctorName)}</Avatar>
        )}
      </div>
      <div className="card-details">
        <div className="card-header">
          <p>{appointment.date}</p>
        </div>
        <div className="card-body">
          <p><strong>Doctor:</strong> {appointment?.doctorName}</p>
          <p><strong>Time:</strong> {appointment.time}</p>
          <p><strong>Department:</strong> {appointment?.doctorDepartment}</p>
          <div><strong>Status:</strong>
            {appointment.status === "BOOKED" && (
              <Chip label="Booked" color="primary" icon={<HourglassEmpty />} />
            )}
            {appointment.status === "APPROVED" && (
              <Chip label="Approved" color="success" icon={<CheckCircleOutline />} />
            )}
            {appointment.status === "COMPLETED" && (
              <Chip label="Completed" color="success" icon={<DoneAll />} />
            )}
            {appointment.status === "CANCELED" && (
              <Chip label="Canceled" color="error" icon={<ErrorOutline />} />
            )}
            {appointment.status === "EXPIRED" && (
              <Chip label="Expired" color="warning" icon={<AccessTime />} />
            )}
          </div>
          <p><strong>hospital:</strong> {appointment?.hospitalName}</p>
          <p><strong>Address:</strong> {appointment.hospitalAddress}</p>
          {appointment.status === "CANCELED" && <p><strong>Cancellation Reason:</strong> {appointment?.cancellationReason}</p>}
        </div>
        <div>
          {(appointment.status !== "COMPLETED") && <Button color="error" variant="outlined" endIcon={<CancelPresentationOutlined/>} onClick={handleOpenModal} fullWidth sx={{mt:2}} disabled={(appointment.status === "CANCELED" || appointment.status === "EXPIRED")}>Cancel Appointment</Button>}
          {(appointment.status === "COMPLETED") && <Button color="success" variant="outlined" endIcon={<FeedbackOutlined/>} onClick={openFeedbackModal} fullWidth sx={{mt:2}} >Submit Feedback</Button>}
        </div>
      </div>
      <Dialog open={openModal} onClose={handleCloseModal}>
        <DialogTitle>Cancel Appointment</DialogTitle>
        <DialogContent>
          <p>Are you sure you want to cancel this appointment?</p>
          <TextField
            label="Cancellation Reason"
            multiline
            rows={4}
            variant="outlined"
            fullWidth
            value={appointmentData.cancellationReason}
            onChange={handleCancellationReasonChange}
            sx={{ mt: 2 }}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseModal} color="primary">Cancel</Button>
          <Button onClick={handleAppointmentCancellation} loading={loading} color="error">Confirm</Button>
        </DialogActions>
      </Dialog>

      <FeedbackModal
       feedbackModal={feedbackModal}
       closeFeedbackModal={closeFeedbackModal}
       appointmentId={appointment.id}
      />
    </div>
  );
};

AppointmentCard.propTypes = {
  appointment: PropTypes.shape({
    id: PropTypes.number.isRequired,
    date: PropTypes.string,
    time: PropTypes.string,
    doctorName: PropTypes.string.isRequired,
    doctorDepartment: PropTypes.string.isRequired,
    doctorImage: PropTypes.string.isRequired,
    hospitalName: PropTypes.string.isRequired,
    hospitalAddress: PropTypes.string.isRequired,
    appointmentDate: PropTypes.string.isRequired,
    status: PropTypes.string.isRequired,
    cancellationReason: PropTypes.string,
  }).isRequired,
  refresh: PropTypes.func.isRequired,
  setSuccess: PropTypes.func.isRequired,
};

export default AppointmentCard;
