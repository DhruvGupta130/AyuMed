import { LocalHospital, Healing, Visibility, Favorite, Psychology, Vaccines, MedicalServices, Work, HealthAndSafety, LocalHospitalSharp, DashboardCustomizeSharp } from "@mui/icons-material";
import { Card, Col, Row } from "antd";
import Title from "antd/es/typography/Title";
import PropTypes from "prop-types";

const iconMapping = {
  Cardiology: <Favorite fontSize="large" color="error" />,
  Neurology: <Psychology fontSize="large" color="primary" />,
  Ophthalmology: <Visibility fontSize="large" color="secondary" />,
  Orthopedics: <Healing fontSize="large" color="success" />,
  Pediatrics: <LocalHospital fontSize="large" color="info" />,
  Immunology: <Vaccines fontSize="large" color="warning" />,
  Dermatology: <Healing fontSize="large" color="success" />,
  Dentistry: <HealthAndSafety fontSize="large" color="error" />,
  ENT: <MedicalServices fontSize="large" color="disabled" />,
  Gastroenterology: <Favorite fontSize="large" color="error" />,
  Urology: <Healing fontSize="large" color="success" />,
  Psychiatry: <Psychology fontSize="large" color="primary" />,
  ObstetricsAndGynecology: <Work fontSize="large" color="info" />,
  Endocrinology: <MedicalServices fontSize="large" color="warning" />,
  Pulmonology: <LocalHospital fontSize="large" color="secondary" />,
  Nephrology: <Healing fontSize="large" color="success" />,
  Rheumatology: <Healing fontSize="large" color="info" />,
  Hematology: <Favorite fontSize="large" color="error" />,
  Pathology: <MedicalServices fontSize="large" color="disabled" />,
  Radiology: <Visibility fontSize="large" color="primary" />,
  Anesthesiology: <MedicalServices fontSize="large" color="warning" />,
  EmergencyMedicine: <LocalHospitalSharp fontSize="large" color="secondary" />,
};

const HospitalDepartmentsCard = ({ departments }) => {
  return (
      <Row gutter={[16, 16]} justify="center">
          {departments.length > 0 && departments.map((dept, index) => (
          <Col xs={24} sm={12} md={6} key={index}>
              <Card
                hoverable
                style={{
                    textAlign: "center",
                    borderRadius: "8px",
                    boxShadow: "0px 4px 6px rgba(0, 0, 0, 0.1)",
                    height: "100%",
                }}
              >
                {iconMapping[dept] || <DashboardCustomizeSharp fontSize="large" color="secondary" />}
                <Title level={4} style={{ marginTop: "16px" }}>
                    {dept}
                </Title>
              </Card>
          </Col>
          ))}
      </Row>
  
  );
}

HospitalDepartmentsCard.propTypes = {
  departments: PropTypes.arrayOf(PropTypes.string).isRequired,
};

export default HospitalDepartmentsCard;