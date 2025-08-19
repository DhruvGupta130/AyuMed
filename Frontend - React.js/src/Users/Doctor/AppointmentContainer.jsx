import { useState, useEffect } from "react";
import {
  Row,
  Col,
  Card,
  Button,
  Modal,
  Spin,
  message,
  Tag,
  Input,
  Avatar,
  Typography,
  Space,
} from "antd";
import { format } from "date-fns";
import axios from "axios";
import { doctorURL } from "../../Api & Services/Api.js";
import { formatGender, getAvatarText } from "../../Api & Services/Services.js";
import {
  CheckCircleOutlined,
  CheckOutlined,
  ClockCircleFilled,
  ClockCircleOutlined,
  CloseCircleOutlined,
} from "@ant-design/icons";
import PropTypes from "prop-types";

const { TextArea } = Input;
const { Title, Text } = Typography;

const AppointmentContainer = () => {
  const [appointments, setAppointments] = useState([]);
  const [loading, setLoading] = useState(false);

  const fetchAppointments = async () => {
    setLoading(true);
    try {
      const token = localStorage.getItem("token");
      const response = await axios.get(`${doctorURL}/appointments`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setAppointments(response.data);
    } catch (error) {
      message.error(error?.response?.data?.message || "Error fetching appointments data.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAppointments();
  }, []);

  return (
    <div className="profile-container">
      <Card title={<Title level={3}>Appointments</Title>}>
        <Row gutter={[16, 16]}>
          {loading ? (
            <Col span={24} style={{padding: "40px 0" }}>
              <Spin tip="Loading Appointments..." size="large" style={{ display: "block", margin: "auto" }} />
            </Col>
          ) : appointments.length > 0 ? (
            appointments.map((appointment) => (
              <Col xs={24} sm={12} md={8} lg={6} key={appointment.id} style={{ display: "flex" }}>
                <Appointment appointment={appointment} refresh={fetchAppointments} />
              </Col>
            ))
          ) : (
            <Col span={24} style={{ padding: "20px" }}>
              <Text>No appointments available.</Text>
            </Col>
          )}
        </Row>
      </Card>
    </div>
  );
};

const Appointment = ({ appointment, refresh }) => {
  const [openModal, setOpenModal] = useState(false);
  const [loading, setLoading] = useState(false);
  const [appointmentData, setAppointmentData] = useState({
    id: appointment.id,
    cancellationReason: "",
  });

  const appointmentDate = format(new Date(appointment.appointmentDate), "MMMM dd, yyyy");
  const appointmentTime = format(new Date(appointment.appointmentDate), "hh:mm a");

  const handleOpenModal = () => setOpenModal(true);
  const handleCloseModal = () => setOpenModal(false);

  const handleCancellationReasonChange = (e) => {
    setAppointmentData({ ...appointmentData, cancellationReason: e.target.value });
  };

  const handleAppointmentCancellation = async () => {
    if (!appointmentData.cancellationReason) {
      message.error("Enter cancellation reason");
      return;
    }
    setLoading(true);
    try {
      const token = localStorage.getItem("token");
      await axios.put(`${doctorURL}/appointment/cancel`, appointmentData, {
        headers: { Authorization: `Bearer ${token}` },
      });
      message.success("Appointment canceled successfully.");
      refresh();
      handleCloseModal();
    } catch (error) {
      message.error(error.response?.data?.message || "Error while cancelling appointment");
    } finally {
      setLoading(false);
    }
  };

  const handleAppointmentStatusUpdate = async (newStatus) => {
    if (!newStatus) {
      message.error("Invalid status update");
      return;
    }
    setLoading(true);
    try {
      const token = localStorage.getItem("token");
      await axios.put(`${doctorURL}/appointment`, { id: appointment.id, status: newStatus }, {
        headers: { Authorization: `Bearer ${token}` },
      });
      message.success(`Appointment moved to ${newStatus}`);
      refresh();
    } catch (error) {
      message.error(error.response?.data?.message || "Error updating appointment status");
    } finally {
      setLoading(false);
    }
  };

  const getNextStatus = () => {
    switch (appointment.status) {
      case "BOOKED":
        return "APPROVED";
      case "APPROVED":
        return "COMPLETED";
      default:
        return null;
    }
  };

  const nextStatus = getNextStatus();

  return (
    <Card
      hoverable
      style={{
        width: "100%",
        minHeight: "500px",
        display: "flex",
        flexDirection: "column",
        justifyContent: "space-between",
        borderRadius: "10px",
        overflow: "hidden",
        boxShadow: "0px 4px 10px rgba(0,0,0,0.1)",
      }}
      cover={
        <div style={{ textAlign: "center", padding: "15px" }}>
          {appointment.patientImage ? (
            <Avatar src={appointment.patientImage} size={150} shape="square" />
          ) : (
            <Avatar size={120} shape="square" style={{ backgroundColor: "#f56a00" }}>
              {getAvatarText(appointment.patientName)}
            </Avatar>
          )}
        </div>
      }
    >
      <div style={{ flex: 1, fontSize: 16 }}>
        <Title level={4}>{appointment.patientName}</Title>
        <Text strong style={{fontSize: 16}}>Date: </Text> {appointmentDate}<br />
        <Text strong style={{fontSize: 16}}>Time: </Text> {appointmentTime}<br />
        <Text strong style={{fontSize: 16}}>Gender: </Text> {formatGender(appointment.patientGender)}<br />
        <Text strong style={{fontSize: 16}}>Email: </Text> {appointment.patientEmail}<br />
        <Text strong style={{fontSize: 16}}>Mobile: </Text> {appointment.patientMobile}<br />

        <div style={{ marginTop: "10px" }}>
          <Text strong>Status: </Text>
          {appointment.status === "BOOKED" && <Tag color="blue" icon={<ClockCircleOutlined />}>Booked</Tag>}
          {appointment.status === "APPROVED" && <Tag color="green" icon={<CheckCircleOutlined />}>Approved</Tag>}
          {appointment.status === "COMPLETED" && <Tag color="green" icon={<CheckOutlined />}>Completed</Tag>}
          {appointment.status === "CANCELED" && <Tag color="red" icon={<CloseCircleOutlined />}>Canceled</Tag>}
          {appointment.status === "EXPIRED" && <Tag color="orange" icon={<ClockCircleFilled />}>Expired</Tag>}
        </div>
      </div>

      <Space style={{ width: "100%", marginTop: 10 }} direction="vertical">
        {nextStatus && (
          <Button type="primary" style={{backgroundColor: 'green'}} icon={<CheckCircleOutlined/>} onClick={() => handleAppointmentStatusUpdate(nextStatus)} loading={loading} block>
            Move to {formatGender(nextStatus)}
          </Button>
        )}
        <Button type="primary" danger onClick={handleOpenModal} icon={<CloseCircleOutlined/>} block disabled={["COMPLETED", "EXPIRED", "CANCELED"].includes(appointment.status)}>
          Cancel Appointment
        </Button>
      </Space>

      <Modal
        title="Cancel Appointment"
        open={openModal}
        onCancel={handleCloseModal}
        onOk={handleAppointmentCancellation}
        confirmLoading={loading}
        centered
        okText="Confirm Cancellation"
        cancelText="Close"
      >
        <Text>Are you sure you want to cancel this appointment?</Text>
        <TextArea value={appointmentData.cancellationReason} onChange={handleCancellationReasonChange} rows={4} placeholder="Enter cancellation reason" />
      </Modal>
    </Card>
  );
};

Appointment.propTypes = {
  appointment: PropTypes.shape({
    id: PropTypes.number.isRequired,
    patientName: PropTypes.string.isRequired,
    patientImage: PropTypes.string,
    patientGender: PropTypes.string.isRequired,
    patientEmail: PropTypes.string.isRequired,
    patientMobile: PropTypes.string.isRequired,
    appointmentDate: PropTypes.string.isRequired,
    status: PropTypes.string.isRequired,
  }).isRequired,
  refresh: PropTypes.func.isRequired,
};

export default AppointmentContainer;
