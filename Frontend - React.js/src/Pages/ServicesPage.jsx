import { Row, Col, Card, Typography } from "antd";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faStethoscope,
  faPrescriptionBottle,
  faAmbulance,
  faXRay,
  faSyringe,
  faBed,
} from "@fortawesome/free-solid-svg-icons";
import "../Styles/ServicesPage.css";

const { Title, Text } = Typography;

const services = [
  {
    title: "OPD Services",
    description:
      "Our Outpatient Department offers expert consultations with specialists across various fields, ensuring comprehensive and personalized care.",
    icon: faStethoscope,
    color: "#4CAF50",
  },
  {
    title: "Pharmacy",
    description:
      "Our pharmacy provides access to a wide variety of medicines, health supplements, and prescription refills for your convenience.",
    icon: faPrescriptionBottle,
    color: "#FF9800",
  },
  {
    title: "Emergency Care",
    description:
      "Available 24/7, our emergency care unit ensures immediate attention to critical medical situations with advanced facilities.",
    icon: faAmbulance,
    color: "#E91E63",
  },
  {
    title: "Diagnostic Services",
    description:
      "Get accurate and timely diagnosis with our modern diagnostic services, including X-rays, MRIs, blood tests, and more.",
    icon: faXRay,
    color: "#3F51B5",
  },
  {
    title: "Vaccination Services",
    description:
      "We offer a range of vaccinations for all age groups, helping you stay protected against preventable diseases.",
    icon: faSyringe,
    color: "#009688",
  },
  {
    title: "Inpatient Services",
    description:
      "Our inpatient department provides comfortable rooms, dedicated nursing staff, and personalized care for admitted patients.",
    icon: faBed,
    color: "#673AB7",
  },
];

const ServicesPage = () => {
  return (
    <div className="services-page">
      {/* Header Section */}
      <div className="services-header">
        <Title level={1} className="services-title">
          Our Services
        </Title>
        <Text className="services-subtitle">
          At our hospital, we strive to provide top-notch healthcare services with a patient-centric approach. Explore our range of medical facilities designed to cater to your needs.
        </Text>
      </div>

      {/* Services Section */}
      <Row gutter={[24, 24]} justify="center" className="services-grid">
        {services.map((service, index) => (
          <Col xs={24} sm={12} md={8} key={index}>
            <Card hoverable className="service-card">
              <div className="service-icon" style={{ backgroundColor: service.color }}>
                <FontAwesomeIcon icon={service.icon} size="2x" color="#fff" />
              </div>
              <Title level={4} className="service-title">
                {service.title}
              </Title>
              <Text className="service-description">{service.description}</Text>
            </Card>
          </Col>
        ))}
      </Row>
    </div>
  );
};

export default ServicesPage;
