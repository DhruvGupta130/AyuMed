import { useState, useEffect } from "react";
import { Table, Card, Typography, Spin, Alert, Button, Modal, Form, Input } from "antd";
import axios from "axios";
import { doctorURL } from "../../Api & Services/Api.js";
import { formatGender } from "../../Api & Services/Services.js";
import LabTestView from "./LabTestView.jsx";

const { Title } = Typography;

const Patients = () => {
  const [patients, setPatients] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [form] = Form.useForm();
  const [selectedPatientId, setSelectedPatientId] = useState(null);
  const [medicalRecords, setMedicalRecords] = useState([]);
  const [recordLoading, setRecordLoading] = useState(false);
  const [selectedPatient, setSelectedPatient] = useState(null);
  const [medicalId, setMedicalId] = useState('');
  const [visible, setVisible] = useState(false);
  const [modalVisible, setModalVisible] = useState(false);
  const [medicalHistory, setMedicalHistory] = useState({
    problems: '',
    diagnosisDetails: '',
    medications: [],
    treatmentPlan: '',
    followUpInstructions: '',
    notes: ''
  });

  useEffect(() => {
    const fetchPatients = async () => {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get(`${doctorURL}/patients`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        setPatients(response.data);
      } catch (err) {
        console.error(err);
        setError(err?.response?.data?.message || "Error while fetching patients. Please try again!");
      } finally {
        setLoading(false);
      }
    };

    fetchPatients();
  }, []);

  const handleAddMedicalHistory = (patientId) => {
    setSelectedPatientId(patientId);
    setIsModalVisible(true);
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
      const response = await axios.get(`${doctorURL}/medical-histories/${patientId}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setMedicalRecords(response.data);
    } catch (err) {
      setError(err?.response?.data?.message || "Error fetching medical history. Please try again!");
    } finally {
      setRecordLoading(false);
    }
  };

  const handleSubmitMedicalHistory = async () => {
    try {
      const token = localStorage.getItem("token");
      await axios.post(`${doctorURL}/add-medical-history/${selectedPatientId}`, { ...medicalHistory }, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setIsModalVisible(false);
      form.resetFields();
    } catch (error) {
      console.error("Failed to submit medical history", error);
    }
  };

  const handleCancel = () => {
    setIsModalVisible(false);
    form.resetFields();
  };

  const onFormChange = (changedValues) => {
    setMedicalHistory(prevState => ({ ...prevState, ...changedValues }));
  };

  const columns = [
    {
      title: "Patient Name",
      dataIndex: "fullName",
      key: "fullName",
    },
    {
      title: "Gender",
      dataIndex: "gender",
      key: "gender",
    },
    {
      title: "Date of Birth",
      dataIndex: "dateOfBirth",
      key: "dateOfBirth",
      responsive: ["md"]
    },
    {
      title: "Email",
      dataIndex: "email",
      key: "email",
    },
    {
      title: "Mobile",
      dataIndex: "mobile",
      key: "mobile",
    },
    {
      title: "Medical Record",
      key: "actions",
      width: "1%",
      render: (text, record) => (
        <div className="button-container">
          <Button style={{marginBottom: 3}} type="primary" onClick={() => handleViewMedicalHistory(record.id, record.fullName)}>
            View Record
          </Button>
          <Button style={{marginTop: 3, backgroundColor: '#389e0d' }} type="primary"  onClick={() => handleAddMedicalHistory(record.id)}>
            Add a Record
          </Button>
        </div>
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
        <Button type="primary" onClick={() => handleViewLabTest(record.id)}>
          View Test
        </Button>
      ),
    },
  ];

  return (
    <div className="hospital-patients">
      <Card title={<Title level={3}>Patients List</Title>} >
        {loading ? (
          <Spin tip="Loading Patients..." size="large" style={{ display: "block", margin: "auto" }} />
        ) : error ? (
          <Alert message={error} type="error" showIcon />
        ) : (
          <Table
            dataSource={patients.map((patient) => ({
              ...patient,
              key: patient.id,
              gender: `${formatGender(patient.gender)}`,
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

      {/* Modal for adding medical history */}
      <Modal
        title="Add Medical History"
        open={isModalVisible}
        onOk={handleSubmitMedicalHistory}
        onCancel={handleCancel}
        footer={[
          <Button key="back" onClick={handleCancel}>
            Cancel
          </Button>,
          <Button key="submit" type="primary" onClick={handleSubmitMedicalHistory}>
            Submit
          </Button>,
        ]}
      >
        <Form
          form={form}
          layout="vertical"
          onValuesChange={onFormChange}
        >
          <Form.Item label="Problems" name="problems">
            <Input.TextArea rows={3} value={medicalHistory.problems} />
          </Form.Item>

          <Form.Item label="Diagnosis Details" name="diagnosisDetails">
            <Input.TextArea rows={4} value={medicalHistory.diagnosisDetails} />
          </Form.Item>

          <Form.Item label="Medications" name="medications">
            <Input.TextArea
              rows={4}
              value={medicalHistory.medications.join(", ")}
              onChange={(e) => setMedicalHistory(prevState => ({ ...prevState, medications: e.target.value.split(",") }))}
              placeholder="Enter medications separated by commas"
            />
          </Form.Item>

          <Form.Item label="Treatment Plan" name="treatmentPlan">
            <Input.TextArea rows={4} value={medicalHistory.treatmentPlan} />
          </Form.Item>

          <Form.Item label="Follow-up Instructions" name="followUpInstructions">
            <Input.TextArea rows={4} value={medicalHistory.followUpInstructions} />
          </Form.Item>

          <Form.Item label="Notes" name="notes">
            <Input.TextArea rows={4} value={medicalHistory.notes} />
          </Form.Item>
        </Form>
      </Modal>

      <LabTestView
        visible={visible}
        setVisible={setVisible}
        medicalId={medicalId}
      />
    </div>
  );
};

export default Patients;
