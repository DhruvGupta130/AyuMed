import { useNavigate } from "react-router-dom";
import { Box, Typography } from "@mui/material";
import { Card, Row, Col } from "antd";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faHospital,
  faPrescriptionBottle,
  faCalendarCheck,
  faUserMd,
  faPills,
} from "@fortawesome/free-solid-svg-icons";
import Title from "antd/es/typography/Title";

const services = [
  {
    title: "Find Nearby Hospitals",
    description: "Locate hospitals near you with advanced facilities and expert care.",
    icon: faHospital,
    route: "/nearby-hospitals",
  },
  {
    title: "Find Nearby Pharmacies",
    description: "Discover pharmacies in your area for quick access to medicines.",
    icon: faPrescriptionBottle,
    route: "/nearby-pharmacies",
  },
  {
    title: "Book an Appointment",
    description: "Schedule a visit with a doctor at a nearby hospital.",
    icon: faCalendarCheck,
    route: "/page/appointment/book",
  },
  {
    title: "Consult a Doctor",
    description: "Get instant medical consultation with expert doctors.",
    icon: faUserMd,
    route: "/page/consult-doctor",
  },
  {
    title: "Order Medicines Online",
    description: "Get your medicines delivered to your doorstep with ease.",
    icon: faPills,
    route: "/page/order-medicines",
  },
];

function Info() {
  const navigate = useNavigate();

  return (
    <Box sx={{ bgcolor: "#f5f5f5", padding: 2 }}>
      <Card
        style={{padding: 20, background: "#f9f9f9", borderRadius: 12 }}
        title={
          <Title level={2} style={{textAlign: 'center', textWrap: 'wrap', fontWeight: 700}}>
            Explore Healthcare Services
          </Title>
        }
      >
        <Typography variant="body1" align="center" color="textSecondary" gutterBottom>
          Quickly find hospitals, pharmacies, and doctors near you. Access medical care with ease.
        </Typography>

        <Row gutter={[24, 24]} justify="center" style={{ marginTop: 20 }}>
          {services.map((service, index) => (
            <Col xs={24} sm={12} md={8} key={index}>
              <Card
                hoverable
                className="info-card"
                onClick={() => navigate(service.route)}
                style={{ textAlign: "center", padding: "16px", borderRadius: "8px", boxShadow: "0px 4px 6px rgba(0, 0, 0, 0.1)", height: "100%" }}
              >
                <FontAwesomeIcon icon={service.icon} size="3x" style={{ color: "#1890ff", marginBottom: "15px" }} />
                <Typography variant="h6" fontWeight="bold" gutterBottom>
                  {service.title}
                </Typography>
                <Typography variant="body2" color="textSecondary">
                  {service.description}
                </Typography>
              </Card>
            </Col>
          ))}
        </Row>
      </Card>
    </Box>
  );
}

export default Info;
