import { useEffect, useState } from "react";
import { Row, Col, Card, Typography } from "antd";
import { LocalHospital, Healing, Visibility, Favorite, Psychology, Vaccines, MedicalServices, Work, HealthAndSafety, LocalHospitalSharp } from "@mui/icons-material";
import axios from "axios";
import { URL } from "../Api & Services/Api";
import { Box } from "@mui/material";

const { Title, Paragraph } = Typography;

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

const departmentDescriptions = {
  Cardiology: "We provide comprehensive care for heart-related conditions.",
  Neurology: "Specialized care for neurological disorders and diseases.",
  Ophthalmology: "Expert treatment for eye-related issues and vision care.",
  Orthopedics: "We treat bone, joint, and muscle problems.",
  Pediatrics: "Comprehensive healthcare for children from infancy to adolescence.",
  Immunology: "Focused care for autoimmune diseases and allergies.",
  Dermatology: "Skin health and care, including treatment for various conditions.",
  Dentistry: "Comprehensive dental care, including preventive and cosmetic treatments.",
  ENT: "Care for conditions affecting the ears, nose, and throat.",
  Gastroenterology: "Specialized treatment for digestive system disorders.",
  Urology: "Treatment for urinary tract and male reproductive system conditions.",
  Psychiatry: "Mental health services, including counseling and treatment for disorders.",
  ObstetricsAndGynecology: "Specialized care for women, including childbirth and reproductive health.",
  Endocrinology: "Focused care for hormonal and metabolic disorders.",
  Pulmonology: "Specialized care for respiratory system conditions.",
  Nephrology: "Expert care for kidney-related disorders.",
  Rheumatology: "Treatment for autoimmune diseases and musculoskeletal conditions.",
  Hematology: "Specialized care for blood disorders, including anemia and clotting disorders.",
  Pathology: "Diagnosis of diseases through laboratory tests and examinations.",
  Radiology: "Advanced imaging techniques for diagnosing and monitoring diseases.",
  Anesthesiology: "Expert care in anesthesia administration for surgeries and procedures.",
  EmergencyMedicine: "Immediate care for acute medical conditions and emergencies."
};

function DepartmentsSection() {
  const [departments, setDepartments] = useState([]);

  const fetchDepartments = async () => {
    try {
      const response = await axios.get(`${URL}/departments`);
      console.log(response.data);
      setDepartments(response.data);
    } catch (error) {
      console.error("Error: ", error);
    }
  };

  useEffect(() => {
    fetchDepartments();
  }, []);

  return (
    <Box sx={{ bgcolor: "#f5f5f5", padding: 5 }}>
        <Card
            style={{
                padding: 30,
                background: "#f9f9f9",
                borderRadius: 12,
            }}
            title={
                <Title level={2} style={{textAlign: 'center', textWrap: 'wrap', fontWeight: 700}}>
                    Our Medical Departments
                </Title>
            }
        >
            <Paragraph style={{ textAlign: "center" }}>
                We provide specialized medical care across various departments.
            </Paragraph>
            <Row gutter={[16, 16]} justify="center">
                {departments.length > 0 && departments.slice(0, 4).map((dept, index) => (
                <Col xs={24} sm={12} md={8} key={index}>
                    <Card
                    hoverable
                    style={{
                        textAlign: "center",
                        padding: "16px",
                        borderRadius: "8px",
                        boxShadow: "0px 4px 6px rgba(0, 0, 0, 0.1)",
                        height: "100%",
                    }}
                    >
                    {iconMapping[dept] || <MedicalServices fontSize="large" color="disabled" />}
                    <Title level={4} style={{ marginTop: "16px" }}>
                        {dept}
                    </Title>
                    <Paragraph>{departmentDescriptions[dept] || "No description available."}</Paragraph>
                    </Card>
                </Col>
                ))}
                <Col xs={24} sm={12} md={8}>
                <Card
                    hoverable
                    style={{
                    textAlign: "center",
                    padding: "16px",
                    borderRadius: "8px",
                    boxShadow: "0px 4px 6px rgba(0, 0, 0, 0.1)",
                    height: "100%",
                    }}
                >
                    <MedicalServices fontSize="large" color="disabled" />
                    <Title level={4} style={{ marginTop: "16px" }}>
                        More Departments
                    </Title>
                    <Paragraph>
                        Discover more specialized departments for your healthcare needs.
                    </Paragraph>
                </Card>
                </Col>
            </Row>
        </Card>
    </Box>
  );
}

export default DepartmentsSection;
