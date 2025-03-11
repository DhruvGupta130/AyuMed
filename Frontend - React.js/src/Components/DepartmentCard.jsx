import "react";
import { Card, Typography } from "antd";
import { LocalHospital, Healing, Visibility, Favorite, Psychology, Vaccines, MedicalServices, Work, HealthAndSafety, LocalHospitalSharp, DashboardCustomizeSharp } from "@mui/icons-material";
import PropTypes from "prop-types";

const { Title } = Typography;

// 🎯 Department Icon Mapping
const departmentIcons = {
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

// 🏥 Department Card Component
const DepartmentCard = ({ name, onSelect, selected }) => {
  return (
    <Card
      hoverable
      onClick={onSelect}
      style={{
        backgroundColor: selected === name ? "#89CFF0" : "#f0f2f5",
        width: 320,
        textAlign: "center",
        borderRadius: 10,
        cursor: "pointer",
        transition: "0.3s",
      }}
    >
        {departmentIcons[name] || <DashboardCustomizeSharp fontSize="large" color="secondary" />}

      <Title level={5} style={{ marginTop: 10 }}>
        {name}
      </Title>
    </Card>
  );
};

DepartmentCard.propTypes = {
    name: PropTypes.string.isRequired,
    onSelect: PropTypes.func.isRequired,
    selected: PropTypes.string,
};

export default DepartmentCard;
