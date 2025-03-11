import { useState, useEffect } from "react";
import axios from "axios";
import { patientURL } from "../../Api & Services/Api.js";
import AppointmentCard from "./AppointmentCard.jsx";
import { Alert, Button, CircularProgress, Dialog, DialogActions, DialogContent, DialogTitle, FormControl, InputLabel, Select, MenuItem, TextField } from "@mui/material";
import {CalendarMonthOutlined, CheckCircleOutline, TransferWithinAStationOutlined} from "@mui/icons-material";
import { convertTo12HourFormat } from "../../Api & Services/Services.js";
import {useNavigate} from "react-router-dom";

const Appointments = () => {
  const [appointments, setAppointments] = useState([]);
  const [loading, setLoading] = useState(false);
  const [load, setLoad] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState('');
  const [openModal, setOpenModal] = useState(false);
  const [selectedDepartment, setSelectedDepartment] = useState("");
  const [selectedHospital, setSelectedHospital] = useState("");
  const [selectedDoctor, setSelectedDoctor] = useState("");
  const [selectedSlot, setSelectedSlot] = useState({
    slotIndex: ''
  });
  const [departments, setDepartments] = useState([]);
  const [hospitals, setHospitals] = useState([]);
  const [doctors, setDoctors] = useState([]);
  const [selectedDate, setSelectedDate] = useState("");
  const [slots, setSlots] = useState([]);
  const [, setDropdownLoading] = useState({
    departments: false,
    hospitals: false,
    doctors: false,
    slots: false,
  });

  const navigate = useNavigate();

  const fetchAppointments = async () => {
    setLoading(true);
    try {
      const token = localStorage.getItem("token");
      const response = await axios.get(`${patientURL}/appointments`, {
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });
      setAppointments(response.data);
    } catch (error) {
      setError("Error fetching appointments data.");
      console.error('Error fetching appointments:', error);
    } finally {
      setLoading(false);
    }
  };

  const fetchDepartments = async () => {
    setDropdownLoading((prev) => ({ ...prev, departments: true }));
    try {
      const token = localStorage.getItem("token");
      const response = await axios.get(`${patientURL}/departments`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setDepartments(response.data);
    } catch (error) {
      setError("Error fetching departments data.");
      console.error("Error fetching departments:", error);
    } finally {
      setDropdownLoading((prev) => ({ ...prev, departments: false }));
    }
  };

  const fetchHospitals = async (department) => {
    setDropdownLoading((prev) => ({ ...prev, hospitals: true }));
    try {
      const token = localStorage.getItem("token");
      const response = await axios.get(`${patientURL}/${department}/hospitals`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setHospitals(response.data);
      console.log(response.data);
    } catch (error) {
      setError("Error fetching hospitals data.");
      console.error("Error fetching hospitals:", error);
    } finally {
      setDropdownLoading((prev) => ({ ...prev, hospitals: false }));
    }
  };

  const fetchDoctors = async (department, hospitalId) => {
    setDropdownLoading((prev) => ({ ...prev, doctors: true }));
    try {
      const token = localStorage.getItem("token");
      const response = await axios.get(
        `${patientURL}/${department}/${hospitalId}/doctors`,
        { headers: { Authorization: `Bearer ${token}` } }
      );
      setDoctors(response.data);
    } catch (error) {
      setError("Error fetching doctors data.");
      console.error("Error fetching doctors:", error);
    } finally {
      setDropdownLoading((prev) => ({ ...prev, doctors: false }));
    }
  };

  const fetchSlots = async (doctorId, date) => {
    setDropdownLoading((prev) => ({ ...prev, slots: true }));
    try {
      const token = localStorage.getItem("token");
      const response = await axios.get(`${patientURL}/${doctorId}/slots?date=${date}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setSlots(response.data);
      console.log(response.data);
    } catch (error) {
      setError("Error fetching slots data.");
      console.error("Error fetching slots:", error);
    } finally {
      setDropdownLoading((prev) => ({ ...prev, slots: false }));
    }
  };

  const getTimeSlot = (slot) => {
    if (!slot || !slot.startTime || !slot.endTime) {
      return 'Invalid time slot';
    }
    const startTimeFormatted = convertTo12HourFormat(slot.startTime);
    const endTimeFormatted = convertTo12HourFormat(slot.endTime);
    const availability = slot.available ? 'Available' : 'Not Available';
    return `${startTimeFormatted} - ${endTimeFormatted} (${availability})`;
  };


  const handleNewAppointment = () => {
    setOpenModal(true);
    fetchDepartments();
  };

  const handleCloseModal = () => {
    setOpenModal(false);
    setSelectedDepartment("");
    setSelectedHospital("");
    setSelectedDoctor("");
    setSelectedSlot("");
  };

  const handleBookAppointment = async () => {
    setLoad(true);
    try {
      const token = localStorage.getItem("token");
      const response = await axios.post(`${patientURL}/appointment`,
        {
          doctorId: selectedDoctor.id,
          slot: selectedSlot.slotIndex,
          appointmentDate: selectedDate,
        },
        { headers: { Authorization: `Bearer ${token}` } }
      );
      setSuccess(response.data.message);
      setTimeout(() => setSuccess(""), 2000);
      handleCloseModal();
      await fetchAppointments();
      setSelectedDate('');
    } catch (error) {
      setError(error?.response?.data?.message || "Error booking appointment.");
      console.error("Error booking appointment:", error);
    } finally {
      setLoad(false);
    }
  };

  useEffect(() => {
    fetchAppointments();
  }, []);

  useEffect(() => {
    if (selectedDepartment) fetchHospitals(selectedDepartment);
  }, [selectedDepartment]);

  useEffect(() => {
    if (selectedDepartment && selectedHospital) fetchDoctors(selectedDepartment, selectedHospital.id);
  }, [selectedDepartment, selectedHospital]);

  useEffect(() => {
    if (selectedDoctor && selectedDate) fetchSlots(selectedDoctor.id, selectedDate);
  }, [selectedDoctor, selectedDate]);

  if (loading) {
    return <div className="loader"><CircularProgress /></div>;
  }

  if (error) {
    return (
      <div className='error-box'>
        <Alert sx={{ display: "flex", justifyContent: "center", fontSize: "medium" }} severity="error">
          {error}
        </Alert>
      </div>
    );
  }

  return (
    <div className="appointments-box">
      {success && <Alert sx={{ display: "flex", justifyContent: "center", fontSize: "medium" }} severity="success">
          {success}
      </Alert>}
      <div className={`upcoming-appointments ${appointments.length === 0 ? 'flex-display' : ''}`}>
        {appointments.length > 0 ? (
          <div className="appointments-list">
            {appointments.map((appointment) => (
              <AppointmentCard key={appointment.id} 
                appointment={appointment} 
                setSuccess={setSuccess} 
                refresh={fetchAppointments}
               />
            ))}
          </div>
        ) : (
          <div />
        )}
        <div className="new-appointment">
          <div className="book-now">
            <p>Schedule your next visit with ease – Book an appointment now!</p>
            <CheckCircleOutline sx={{ height: 40, width: 40 }} />
          </div>
          <div>
            <Button
              variant="contained"
              onClick={handleNewAppointment}
              startIcon={<CalendarMonthOutlined />}
            >
              Book Appointment
            </Button>
          </div>
        </div>

        <Dialog open={openModal} onClose={handleCloseModal} fullWidth>
          <DialogTitle>Book an Appointment</DialogTitle>
          <DialogContent sx={{padding: '20px' }}>
            <FormControl fullWidth margin="normal">
              <InputLabel>Department</InputLabel>
              <Select
                value={selectedDepartment || ""}
                onChange={(e) => setSelectedDepartment(e.target.value)}
                label="Department"
              >
                {departments.map((department, index) => (
                  <MenuItem key={index} value={department}>
                    {department}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>

            <FormControl fullWidth margin="normal">
              <InputLabel>Hospital</InputLabel>
              <Select
                value={selectedHospital || ""}
                onChange={(e) => setSelectedHospital(e.target.value)}
                label="Hospital"
                disabled={!selectedDepartment}
              >
                {hospitals.map((hospital) => (
                  <MenuItem key={hospital.id} value={hospital}>
                    {hospital.hospitalName}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>

            <FormControl fullWidth margin="normal">
              <InputLabel>Doctor</InputLabel>
              <Select
                value={selectedDoctor || ""}
                onChange={(e) => setSelectedDoctor(e.target.value)}
                label="Doctor"
                disabled={!selectedHospital}
              >
                {doctors.map((doctor) => (
                  <MenuItem key={doctor.id} value={doctor}>
                    {doctor.fullName}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>

            <FormControl fullWidth margin="normal">
              <TextField
                label="Date"
                type="date"
                value={selectedDate}
                onChange={(e) => setSelectedDate(e.target.value)}
                slotProps={{
                  inputLabel:{
                    shrink: true,
                  }
                }}
                fullWidth
              />
            </FormControl>

            <FormControl fullWidth margin="normal">
              <InputLabel>Slot</InputLabel>
              <Select
                value={slots.length === 0 && selectedDate? "noSlots" : selectedSlot || ""}
                onChange={(e) => setSelectedSlot(e.target.value)}
                label="Slot"
                disabled={!selectedDoctor || !selectedDate || slots.length === 0}
              >
                {slots.length === 0 ? (
                  <MenuItem value="noSlots" disabled>
                    No slots available
                  </MenuItem>
                ) : (
                  slots.map((slot, index) => (
                    <MenuItem key={index} value={slot} disabled={!slot.available}>
                      {getTimeSlot(slot)}
                    </MenuItem>
                  ))
                )}
              </Select>
            </FormControl>
          </DialogContent>

          <Button variant="contained" sx={{marginX: 3}} endIcon={<TransferWithinAStationOutlined/>} onClick={() => navigate("/page/appointment/book")}>Open Page in Full Window</Button>

          <DialogActions>
            <Button onClick={handleCloseModal} color="error">
              Cancel
            </Button>
            <Button onClick={handleBookAppointment} color="success" loading={load}>
              Book Appointment
            </Button>
          </DialogActions>
        </Dialog>
      </div>
    </div>
  );
};

export default Appointments;
