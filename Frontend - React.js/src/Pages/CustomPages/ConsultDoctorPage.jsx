import {useState, useEffect, useCallback} from "react";
import axios from "axios";
import { Row, Col, Typography, Button, Select, Modal, message, Spin, Input } from "antd";
import { CheckOutlined, SearchOutlined } from "@ant-design/icons";
import { MenuItem, TextField } from "@mui/material";
import { patientURL } from "../../Api & Services/Api";
import DoctorProfileCard from "../../Components/DoctorProfileCard";
import { convertTo12HourFormat } from "../../Api & Services/Services";
import { useNavigate, useSearchParams } from "react-router-dom";

const { Title} = Typography;
const { Option } = Select;

const ConsultDoctorPage = () => {
  const [doctors, setDoctors] = useState([]);
  const [filteredDoctors, setFilteredDoctors] = useState([]);
  const [specialties, setSpecialties] = useState([]);
  const [departments, setDepartments] = useState([]);
  const [loading, setLoading] = useState(false);
  const [bookingLoading, setBookingLoading] = useState(false);
  const [bookingModal, setBookingModal] = useState(false);
  const [selectedDoctor, setSelectedDoctor] = useState(null);
  const [selectedDate, setSelectedDate] = useState("");
  const [slots, setSlots] = useState([]);
  const [selectedSlot, setSelectedSlot] = useState(null);
  const [searchParams, setSearchParams] = useSearchParams();

  const selectedDepartment = searchParams.get("department") || "";
  const selectedSpecialty = searchParams.get("specialty") || "";
  const searchQuery = searchParams.get("search") || "";

  const navigate = useNavigate();

  const fetchDoctors = useCallback(async () => {
    setLoading(true);
    try {
      const token = localStorage.getItem("token");
      const response = await axios.get(`${patientURL}/doctors`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setDoctors(response.data);
      setFilteredDoctors(response.data);
      setSpecialties([...new Set(response.data.map((doc) => doc.speciality))]);
      setDepartments([...new Set(response.data.map((doc) => doc.department))]);
    } catch (err) {
      console.error(err);
      message.error(err.response.data.message || "Error fetching doctors.");
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchDoctors();
  }, [fetchDoctors]);

  const applyFilters = useCallback((department, specialty, search) => {
    let filtered = doctors;
    if (department) {
      filtered = filtered.filter((doc) => doc.department === department);
    }
    if (specialty) {
      filtered = filtered.filter((doc) => doc.speciality === specialty);
    }
    if (search) {
      filtered = filtered.filter((doc) =>
          doc.fullName.toLowerCase().includes(search.toLowerCase())
      );
    }
    setFilteredDoctors(filtered);
  }, [doctors]);

  useEffect(() => {
    applyFilters(selectedDepartment, selectedSpecialty, searchQuery);
  }, [selectedDepartment, selectedSpecialty, searchQuery, doctors, applyFilters]);

  const getTimeSlot = (slot) => {
    if (!slot || !slot.startTime || !slot.endTime) {
      return 'Invalid time slot';
    }
    const startTimeFormatted = convertTo12HourFormat(slot.startTime);
    const endTimeFormatted = convertTo12HourFormat(slot.endTime);
    const availability = slot.available ? 'Available' : 'Not Available';
    return `${startTimeFormatted} - ${endTimeFormatted} (${availability})`;
  };

  const handleSearchChange = (value) => {
    setSearchParams({ search: value, department: selectedDepartment, specialty: selectedSpecialty });
  };

  const handleFilterChange = (filterType, value) => {
    setSearchParams((prev) => {
      const params = new URLSearchParams(prev);
      params.set(filterType, value);
      return params;
    });
  };

  const fetchSlots = async (doctorId, date) => {
    try {
      const token = localStorage.getItem("token");
      const res = await axios.get(`${patientURL}/${doctorId}/slots?date=${date}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setSlots(res.data);
    } catch (err) {
      console.error(err);
      message.error(err.message.data.message || "Error fetching slots.");
    }
  };

  const handleBookAppointment = async () => {
    if (!selectedDoctor || !selectedDate || !selectedSlot) {
      message.warning("Please select a date and time slot.");
      return;
    }
    
    setBookingLoading(true);

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
      message.success(response.data.message || "Appointment booked successfully!");
      setBookingModal(false);
      navigate("/patient/appointments");
    } catch (err) {
      console.log(err);
      message.error("Error booking appointment.");
    } finally {
      setBookingLoading(false);
    }
  };

  return (
    <div style={{ padding: 30 }}>
      <Title level={2} style={{ textAlign: "center", marginBottom: 20 }}>Consult a Doctor</Title>

      {/* Filters */}
      <Row gutter={[24, 24]} justify="center" style={{ marginBottom: 20 }}>

      <Col xs={24} sm={12} md={8}>
          <Input
            placeholder="Search doctor..."
            size="large"
            allowClear
            value={searchQuery}
            onChange={(e) => handleSearchChange(e.target.value)}
            style={{
              width: "100%",
              borderRadius: 6,
              boxShadow: "0px 2px 5px rgba(0, 0, 0, 0.1)",
            }}
            prefix={<SearchOutlined />}
          />
        </Col>

        <Col xs={24} sm={12} md={8}>
          <Select
            placeholder="Filter by Department"
            style={{ width: "100%", borderRadius: "6px", height: '100%' }}
            value={selectedDepartment}
            onChange={(value) => handleFilterChange("department", value)}
          >
            <Option value="">All Departments</Option>
            {departments.map((dept, index) => (
              <Option key={index} value={dept}>{dept}</Option>
            ))}
          </Select>
        </Col>
        <Col xs={24} sm={12} md={8}>
          <Select
            placeholder="Filter by Specialty"
            style={{ width: "100%", borderRadius: "6px", height: '100%' }}
            value={selectedSpecialty}
            onChange={(value) => handleFilterChange("specialty", value)}
          >
            <Option value="">All Specialties</Option>
            {specialties.map((spec, index) => (
              <Option key={index} value={spec}>{spec}</Option>
            ))}
          </Select>
        </Col>
      </Row>

      {/* Doctor List */}
      {loading ? <Spin size="large" style={{ display: "block", margin: "auto", marginTop: "50px" }} /> : (
        <DoctorProfileCard doctors={filteredDoctors} setOpenModal={setBookingModal} setSelectedDoctor={setSelectedDoctor} />
      )}

      {/* Booking Modal */}
      <Modal
        open={bookingModal}
        loading={bookingLoading}
        onCancel={() => setBookingModal(false)}
        title="Book an Appointment"
        footer={[
          <Button key="cancel" onClick={() => setBookingModal(false)}>Cancel</Button>,
          <Button key="book" type="primary" icon={<CheckOutlined/>} onClick={handleBookAppointment}>Book Appointment</Button>
        ]}
      >
        <TextField
          label="Select Date"
          type="date"
          fullWidth
          size="medium"
          slotProps={{
            inputLabel: { shrink: true },
          }}
          sx={{
            marginBottom: 2,
            "& .MuiOutlinedInput-root": {
              "& fieldset": { borderColor: "#1976D2" },
              "&:hover fieldset": { borderColor: "#1565C0" },
              "&.Mui-focused fieldset": { borderColor: "#0D47A1" },
            },
          }}
          onChange={(e) => {
            setSelectedDate(e.target.value);
            fetchSlots(selectedDoctor.id, e.target.value);
          }}
        />

        <TextField
          select
          label="Select Slot"
          fullWidth
          size="medium"
          value={slots.length === 0 && selectedDate? "noSlots" : selectedSlot || ""}
          onChange={(e) => setSelectedSlot(e.target.value)}
          disabled={ !selectedDate || slots.length === 0}
          sx={{
            maxWidth: 500,
            bgcolor: "white",
            borderRadius: 1,
            "& .MuiOutlinedInput-root": {
              "& fieldset": { borderColor: "#1976D2" },
              "&:hover fieldset": { borderColor: "#1565C0" },
              "&.Mui-focused fieldset": { borderColor: "#0D47A1" },
            },
          }}
        >
          {slots.length === 0 ? (
              <MenuItem key={slots.length} value="noSlots" disabled>
                No slots available
              </MenuItem>
            ) : (
              slots.map((slot, index) => (
                <MenuItem key={index} value={slot} disabled={!slot.available}>
                  {getTimeSlot(slot)}
                </MenuItem>
              ))
            )}
        </TextField>

      </Modal>
    </div>
  );
};

export default ConsultDoctorPage;
