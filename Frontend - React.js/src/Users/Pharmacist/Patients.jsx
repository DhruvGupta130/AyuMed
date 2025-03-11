import { useState, useEffect } from "react";
import { Table, Card, Typography, Spin, Alert } from "antd";
import axios from "axios";
import { pharmacyURL } from "../../Api & Services/Api.js";
import { formatGender } from "../../Api & Services/Services.js";

const { Title } = Typography;

const Patients = () => {
  const [patients, setPatients] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchPatients = async () => {
    setLoading(true);
    try {
      const token = localStorage.getItem("token");
      const response = await axios.get(`${pharmacyURL}/patients`, {
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


  const columns = [
    { title: "Patient Name", dataIndex: "fullName", key: "fullName" },
    { title: "Gender", dataIndex: "gender", key: "gender" },
    { title: "Email", dataIndex: "email", key: "email", responsive: ["md"] },
    { title: "Mobile", dataIndex: "mobile", key: "mobile" },
    { title: "Date of Birth", dataIndex: "dateOfBirth", key: "dateOfBirth", responsive: ["lg"] },
    { title: "Medication Name", dataIndex: "medicationName", key: "medicationName" },
    { title: "Quantity", dataIndex: "quantity", key: "quantity" },
    { title: 'Total cost', dataIndex: 'cost', key: 'cost', render: (cost) => `₹${cost}` },
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
    </div>
  );
};

export default Patients;
