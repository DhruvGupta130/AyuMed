import {useState, useEffect, useCallback} from "react";
import { useNavigate, useParams } from "react-router-dom";
import axios from "axios";
import { Card, Row, Col, Typography, Button, Carousel, Layout, Tag, Divider, Space, Modal, message, Spin } from "antd";
import { PhoneOutlined, MailOutlined, GlobalOutlined, HomeOutlined, FieldTimeOutlined, ShopOutlined, SafetyCertificateOutlined, HeartOutlined, HddOutlined, MedicineBoxOutlined, LaptopOutlined, StarOutlined, UserSwitchOutlined } from "@ant-design/icons";
import { patientURL } from "../../Api & Services/Api.js";
import { convertTo12HourFormat, stringToList } from "../../Api & Services/Services.js";
import { MenuItem, TextField } from "@mui/material";
import DoctorProfileCard from "../../Components/DoctorProfileCard.jsx";
import HospitalDepartmentsCard from "../../Components/HospitalDepartmentsCard.jsx";

const { Title, Text } = Typography;
const { Content } = Layout;

const HospitalProfilePage = () => {
  const { id } = useParams();
  const [hospital, setHospital] = useState(null);
  const [doctors, setDoctors] = useState([]);
  const [loading, setLoading] = useState(true);
  const [openModal, setOpenModal] = useState(false);
  const [selectedDoctor, setSelectedDoctor] = useState(null);
  const [selectedDate, setSelectedDate] = useState('');
  const [slots, setSlots] = useState([]);
  const [selectedSlot, setSelectedSlot] = useState(null);
  const [bookingLoading, setBookingLoading] = useState(false);

  const navigate = useNavigate();

  const getTimeSlot = (slot) => {
      if (!slot || !slot.startTime || !slot.endTime) {
        return 'Invalid time slot';
      }
      const startTimeFormatted = convertTo12HourFormat(slot.startTime);
      const endTimeFormatted = convertTo12HourFormat(slot.endTime);
      const availability = slot.available ? 'Available' : 'Not Available';
      return `${startTimeFormatted} - ${endTimeFormatted} (${availability})`;
    };

  const fetchHospital = useCallback(async () => {
    setLoading(true);
    try {
      const token = localStorage.getItem("token");
      const response = await axios.get(`${patientURL}/hospital/${id}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setHospital(response.data);
    } catch (err) {
      message.error(err.response.data.message || "Error fetching hospital details.");
      console.log(err);
    } finally {
      setLoading(false);
    }
  }, [id]);

  const fetchDoctors = useCallback(async () => {
    setLoading(true);
    try {
      const token = localStorage.getItem("token");
      const response = await axios.get(`${patientURL}/${id}/doctors`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setDoctors(response.data);
    } catch (err) {
      message.error(err.response.data.message || "Error fetching hospital details.");
      console.log(err);
    } finally {
      setLoading(false);
    }
  }, [id]);

  useEffect(() => {
    fetchHospital();
  }, [fetchHospital]);

  useEffect(() => {
    if(hospital){
      fetchDoctors();
    }
  }, [fetchDoctors, hospital]);

  const fetchSlots = async (doctorId, date) => {
    if (!doctorId || !date) return;
    try {
      const token = localStorage.getItem("token");
      const res = await axios.get(`${patientURL}/${doctorId}/slots?date=${date}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setSlots(res.data);
      setSelectedSlot(null);
    } catch (err) {
      console.error(err);
      message.error(err.response?.data?.message || "Error fetching slots.");
    }
  };

  const handleBookAppointment = async () => {
    if (!selectedDoctor || !selectedDate || !selectedSlot) {
      message.warning("Please select a doctor, date, and slot.");
      return;
    }

    setBookingLoading(true);

    console.log(selectedDoctor.id, selectedSlot.slotIndex, selectedDate);

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
      setOpenModal(false);
      navigate("/patient/appointments");
    } catch (err) {
      message.error(err.response?.data?.message || "Error booking appointment.");
      console.error(err);
    } finally {
      setBookingLoading(false);
    }
  };


  const redirectToMap = () => {
    const { latitude, longitude } = hospital.address;
    window.open(`https://www.google.com/maps?q=${latitude},${longitude}`, "_blank");
  };

  if (loading) return <Spin size="large" style={{ display: "block", margin: "auto", marginTop: "50px" }} />;

  return (
    <Layout>
      <Content>
        <Row gutter={[24, 24]} justify="center" style={{gap: 20}}>
          <Col span={24}>
            <Card title={
                <Title level={2} style={{ textAlign: "center", textWrap: 'wrap', margin: 10, fontWeight: 700 }}>{hospital.hospitalName.toUpperCase()}</Title>
              } style={{margin: 18}}>
              <Row gutter={[24, 24]}>
                {hospital.images?.length > 0 && (
                  <Col xs={24} md={12} style={{ display: "flex" }}>
                    <Card style={{width: '100%'}}>
                      <Carousel autoplay effect="fade" style={{ borderRadius: 8, flex: 1}}>
                        {hospital.images.map((image, index) => (
                          <div key={index}>
                            <img src={image} alt="Hospital" style={{ width: "100%", borderRadius: 8, objectFit: "cover", height: 450 }} />
                          </div>
                        ))}
                      </Carousel>
                    </Card>
                  </Col>
                )}
                <Col xs={24} md={12} style={{ display: "flex" }}>
                  <Card style={{ padding: 10, flex: 1, display: "flex", flexDirection: "column"}}>
                    <Space direction="vertical" size="middle" >
                      <Text style={{ fontSize: 18 }}><HomeOutlined /> {hospital.address.street}, {hospital.address.city}, {hospital.address.state}, {hospital.address.zip}</Text>
                      <Text style={{ fontSize: 18 }}><PhoneOutlined /> {hospital.mobile}</Text>
                      <Text style={{ fontSize: 18 }}><MailOutlined /> {hospital.email}</Text>
                      <Text style={{ fontSize: 18 }}><GlobalOutlined /> <a href={hospital.website} target="_blank" rel="noopener noreferrer">{hospital.website}</a></Text>
                      <Text style={{ fontSize: 18 }}><FieldTimeOutlined /> Established: {hospital.establishedYear}</Text>
                      <Text style={{ fontSize: 18 }}><SafetyCertificateOutlined /> Emergency Services: {hospital.emergencyServices ? "Available" : "Not Available"}</Text>
                      <Text style={{ fontSize: 18 }}><HeartOutlined /> ICU Capacity: {hospital.icuCapacity}</Text>
                      <Text style={{ fontSize: 18 }}><HddOutlined /> Bed Capacity: {hospital.bedCapacity}</Text>
                      <Text style={{ fontSize: 18 }}><ShopOutlined /> Operation Theaters: {hospital.operationTheaters}</Text>
                      <Button type="primary" onClick={redirectToMap}>View on Map</Button>
                    </Space>
                  </Card>
                </Col>
              </Row>

              <Divider/>

              <Card title={
                <Title level={2} style={{ color: '#457b9d', marginBottom: 5, marginTop: 10, paddingTop: 20 }}>
                  <ShopOutlined style={{ marginRight: '8px' }} />
                  Overview
                </Title>
              }>
                <Text style={{ fontSize: '18px' }}>{hospital.overview}</Text>
              </Card>

              <Divider/>

              <Card style={{ marginBottom: 20 }} title={
                  <Title level={2} style={{ color: '#457b9d', paddingTop: 20 }}>
                    <HomeOutlined style={{ marginRight: '8px' }} />
                    Departments
                  </Title>
              }> 
                <HospitalDepartmentsCard departments={hospital.departments} />
              </Card>

              <Divider />

              <Card style={{padding: 20}} title={
                <Title level={2} style={{ color: '#457b9d', paddingTop: 20 }}>
                <MedicineBoxOutlined style={{ marginRight: '8px' }} />
                Specialties
              </Title>
              }>
                <Row>
                  {hospital.specialities.map((spec, index) => (
                    <Tag color="green" key={index} style={{ fontSize: 18, padding: 5, margin: 5 }}>{spec}</Tag>
                  ))}
                </Row>
              </Card>

              <Divider />

              {/* Hospital Technology */}
              <Card title={
                <Title level={2} style={{ color: '#457b9d', paddingTop: 20 }}>
                  <LaptopOutlined style={{ marginRight: '8px' }} />
                  Hospital Technology
                </Title>
              }>
                <ul style={{padding: 20, fontSize: 17}}>
                  {stringToList(hospital.technology).map((tech, index) => (
                    <li key={index}>
                      <strong>{tech.heading}:</strong> {tech.descriptionText}
                    </li>
                  ))}
                </ul>
              </Card>

              <Divider />

              {/* Accreditations */}
              <Card title={
                <Title level={2} style={{ color: '#457b9d', paddingTop: 20 }}>
                  <StarOutlined style={{ marginRight: '8px' }} />
                  Accreditations
                </Title>
                }>
                <ul style={{padding: 20, fontSize: 17}}>
                  {stringToList(hospital.accreditations).map((acc, index) => (
                    <li key={index}>
                      <strong>{acc.heading}:</strong> {acc.descriptionText}
                    </li>
                  ))}
                </ul>
              </Card>

              <Divider />

              <Card title={
                <Title level={2} style={{ color: '#457b9d', paddingTop: 20 }}>
                  <UserSwitchOutlined/>
                  Doctors
                </Title>
              }>
                <DoctorProfileCard doctors={doctors} setOpenModal={setOpenModal} setSelectedDoctor={setSelectedDoctor} />
              </Card>
              
            </Card>
          </Col>
          <Modal
            open={openModal}
            loading={bookingLoading}
            onCancel={() => setOpenModal(false)}
            footer={[
              <Button key="cancel" onClick={() => setOpenModal(false)}>Cancel</Button>,
              <Button key="book" type="primary" onClick={handleBookAppointment}>
                Book Appointment
              </Button>
            ]}
            title="Book Appointment"
          >
            <Text
              style={{
                display: "block",
                width: "100%",
                padding: "10px",
                fontSize: "16px",
                border: "1px solid #d9d9d9",
                borderRadius: "6px",
                background: "#f5f5f5",
                color: "#333",
                marginBottom: 20
              }}
            >
              {selectedDoctor?.fullName || "Select a doctor"}
            </Text>


            <TextField
              label="Select Date"
              type="date"
              fullWidth
              size="medium"
              slotProps={{
                inputLabel: { shrink: true },
              }}
            style={{marginBottom: 20}}
            sx={{
              maxWidth: 500,
              bgcolor: "white",
              borderRadius: 1,
              "& .MuiOutlinedInput-root": {
                "& fieldset": { borderColor: "#1976D2" },
                "&:hover fieldset": { borderColor: "#1565C0" },
                "&.Mui-focused fieldset": { borderColor: "#0D47A1" },
                "& .MuiInputBase-input": {
                    fontSize: "18px",
                    padding: "12px",
                },
                    "& .MuiInputLabel-root": {
                    fontSize: "18px",
                },
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
                  "& .MuiInputBase-input": {
                    fontSize: "18px",
                    padding: "12px",
                  },
                  "& .MuiInputLabel-root": {
                    fontSize: "18px",
                  },
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
        </Row>
      </Content>
    </Layout>
  );
};

export default HospitalProfilePage;
