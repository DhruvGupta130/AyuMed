import { useState, useEffect } from "react";
import { Table, Card, Typography, Spin, Alert, Button, Modal } from "antd";
import axios from "axios";
import { hospitalURL } from "../../Api & Services/Api.js";
import { formatGender } from "../../Api & Services/Services.js";
import LabTestModal from "./LabTestModal";  // Import the new LabTestModal component
import LabTestView from "./LabTestView.jsx";

const { Title } = Typography;

const Patients = () => {
  const [patients, setPatients] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [medicalRecords, setMedicalRecords] = useState([]);
  const [modalVisible, setModalVisible] = useState(false);
  const [recordLoading, setRecordLoading] = useState(false);
  const [selectedPatient, setSelectedPatient] = useState(null);
  const [medicalId, setMedicalId] = useState('');
  const [visible, setVisible] = useState(false);
  const [labModalVisible, setLabModalVisible] = useState(false);
  const [selectedRecordId, setSelectedRecordId] = useState(null);

  const fetchPatients = async () => {
    setLoading(true);
    try {
      const token = localStorage.getItem("token");
      const response = await axios.get(`${hospitalURL}/patients`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setPatients(response.data);
    } catch (err) {
      setError("Error fetching patients. Please try again!");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchPatients();
  }, []);

  const handleOpenLabModal = (recordId) => {
    setSelectedRecordId(recordId);
    setLabModalVisible(true);
  };

  const handleViewLabTest = (medicalId) => {
    setVisible(true);
    setMedicalId(medicalId);
  }

  const handleViewMedicalHistory = async (patientId, patientName) => {
    setModalVisible(true);
    setRecordLoading(true);
    setSelectedPatient(patientName);

    try {
      const token = localStorage.getItem("token");
      const response = await axios.get(`${hospitalURL}/medical-histories/${patientId}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setMedicalRecords(response.data);
    } catch (err) {
      setError(err?.response?.data?.message || "Error fetching medical history. Please try again!");
      console.error(err);
    } finally {
      setRecordLoading(false);
    }
  };


  const columns = [
    { title: "Patient Name", dataIndex: "fullName", key: "fullName" },
    { title: "Gender", dataIndex: "gender", key: "gender" },
    { title: "Email", dataIndex: "email", key: "email", responsive: ["md"] },
    { title: "Mobile", dataIndex: "mobile", key: "mobile" },
    { title: "Date of Birth", dataIndex: "dateOfBirth", key: "dateOfBirth", responsive: ["lg"] },
    { title: "Department", dataIndex: "department", key: "department" },
    { title: "Doctor Name", dataIndex: "doctorName", key: "doctorName" },
    {
      title: "Actions",
      key: "actions",
      render: (_, record) => (
        <Button type="primary" onClick={() => handleViewMedicalHistory(record.id, record.fullName)}>
          View Medical Report
        </Button>
      ),
    },
  ];

  const medicalHistoryColumns = [
    { title: "Diagnosis", dataIndex: "diagnosisDetails", key: "diagnosisDetails" },
    { title: "Problems", dataIndex: "problems", key: "problems" },
    { title: "Medications", dataIndex: "medications", key: "medications", render: meds => meds.join(", ") },
    { title: "Treatment Start", dataIndex: "treatmentStartDate", key: "treatmentStartDate" },
    { title: "Treatment End", dataIndex: "treatmentEndDate", key: "treatmentEndDate" },
    {
      title: "Lab Test",
      key: "actions",
      render: (_, record) => (
        <div className="button-container">
          <Button style={{marginBottom: 3}} type="primary" onClick={() => handleViewLabTest(record.id)}>
            View Test
          </Button>
          <Button style={{marginTop: 3, backgroundColor: '#389e0d' }} type="primary"  onClick={() => handleOpenLabModal(record.id)}>
            Add a Test
          </Button>
        </div>
      ),
    },
  ];

  return (
    <div className="hospital-patients">
      <Card title={<Title level={3}>Patients List</Title>}>
        {loading ? (
          <Spin tip="Loading Patients..." size="large" style={{ display: "block", margin: "auto" }} />
        ) : error ? (
          <Alert message={error} type="error" showIcon />
        ) : (
          <Table
            dataSource={patients.map(patient => ({
              ...patient,
              key: patient.id,
              gender: formatGender(patient.gender),
            }))}
            columns={columns}
            bordered
            pagination={{ pageSize: 10 }}
            scroll={{ x: 800 }} 
          />
        )}
      </Card>

      {/* Medical Records Modal */}
      <Modal
        title={`Medical Record of ${selectedPatient}`}
        open={modalVisible}
        onCancel={() => setModalVisible(false)}
        footer={null}
        width={1000}
        scroll="vertical"
      >
        {recordLoading ? (
          <Spin tip="Loading Medical History..." size="large" style={{ display: "block", margin: "auto" }} />
        ) : medicalRecords.length > 0 ? (
          <Table
            dataSource={medicalRecords.map(record => ({ ...record, key: record.id }))}
            columns={medicalHistoryColumns}
            bordered
            pagination={{ pageSize: 5 }}
            scroll={{ x: 800 }} 
          />
        ) : (
          <Alert message="No medical history found for this patient." type="info" showIcon />
        )}
      </Modal>

      {/* Lab Results Modal */}
      <LabTestModal
        labModalVisible={labModalVisible}
        setLabModalVisible={setLabModalVisible}
        recordId={selectedRecordId}
      />

      <LabTestView
        visible={visible}
        setVisible={setVisible}
        medicalId={medicalId}
      />
    </div>
  );
};

export default Patients;
