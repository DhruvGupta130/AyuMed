import { useEffect, useState } from "react";
import { Card, Spin, Alert, Typography, Button, Modal, Form, Input, message, Select, Avatar, Upload, Row, Col } from "antd";
import { UserOutlined, UserSwitchOutlined, UploadOutlined } from "@ant-design/icons";
import axios from "axios";
import { displayImage, doctorURL, URL } from "../../Api & Services/Api.js";
import "./Doctor.css";
import { LocalHospital } from "@mui/icons-material";
import { fetchDoctorHospital, fetchDoctorProfileData } from "./fetchDoctorProfileData";
import { generateLabel } from "../../Api & Services/Services.js";

const { Title, Text } = Typography;
const { Option } = Select;

const DoctorProfile = () => {
  const [doctor, setDoctor] = useState({
    firstName : ' ',
    lastName : ' ',
    gender : ' ',
    email : ' ',
    mobile : ' ',
    speciality : ' ',
    licenseNumber : ' ',
    department : ' ',
    experience : ' ',
    image : ' ',
    degree : ' ',
  });
  const [hospital, setHospital] = useState({});
  const [loading, setLoading] = useState(true);
  const [load, setLoad] = useState(false);
  const [error, setError] = useState("");
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [newData, setNewData] = useState({
    firstName : ' ',
    lastName : ' ',
    gender : ' ',
    email : ' ',
    mobile : ' ',
    speciality : ' ',
    licenseNumber : ' ',
    department : ' ',
    startDate : ' ',
    image : ' ',
    degree : ' ',
  });
  const [form] = Form.useForm();

  const fetchDoctor = async () => {
    await fetchDoctorProfileData(setDoctor, setLoading, setError);
  }

  const fetchHospital = async () => {
    await fetchDoctorHospital(setHospital, setLoading, setError);
  }

  useEffect(() => {
    fetchDoctor();
    fetchHospital();
  }, []);

  useEffect(() => {
    if (doctor) {
      setNewData({
        ...doctor,
        image: doctor.image || null,
      });
    }
  }, [doctor]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewData((prev) => ({ ...prev, [name]: value }));
  };

  const handleImageChange = ({ file }) => {
    if (file.status === "done") {
      setNewData((prev) => ({ ...prev, image: file.response }));
      message.success("Image uploaded successfully!");
    } else if (file.status === "error") {
      message.error("Image upload failed. Please try again.");
    }
  };

  const handleRemoveImage = async (file) => {
    try {
      await axios.delete(`${URL}/delete-image`, { data: { fileName: file.name } });
      message.success("Image removed successfully.");
    } catch (err) {
      message.error("Failed to remove image. Please try again.");
      console.log("Error in removing image: ",err);
    }
  };

  const openModal = () => setIsModalOpen(true);
  const closeModal = () => setIsModalOpen(false);

  const updateProfile = async () => {
    setLoad(true);
    if (!newData.image) {
      message.error("Please upload a profile photo.");
      return;
    }

    try {
      const token = localStorage.getItem("token");
      const response = await axios.put(`${doctorURL}/updateProfile`, newData, {
        headers: { Authorization: `Bearer ${token}` },
      });
      message.success(response.data.message);
      closeModal();
      await fetchDoctor();
    } catch (err) {
      message.error(err.response?.data?.message || "Failed to update profile. Please try again.");
    } finally {
      setLoad(false);
    }
  };

  return (
    <div className="profile-container">
      <Card >
        {loading ? (
          <Spin tip="Loading Doctor Data..." size="large" style={{ display: "block", margin: "auto" }} />
        ) : error ? (
          <Alert message={error} type="error" showIcon />
        ) : (
          <Row gutter={[24, 24]} style={{ display: 'flex' }}>
            {/* Doctor Profile Section */}
            <Col xs={24} md={12} style={{ display: 'flex', flexDirection: 'column' }}>
              <Card title={<Title level={3}>Doctor Profile</Title>} style={{ flex: 1 }}>
                <Avatar
                  size={200}
                  src={displayImage(doctor?.image)}
                  icon={!doctor?.image ? <UserOutlined /> : null}
                  shape="square"
                  style={{ marginBottom: "16px", borderRadius: "10px" }}
                />
                {[ 
                  { label: "Name", value: doctor.fullName },
                  { label: "Gender", value: doctor.gender },
                  { label: "Email", value: doctor.email },
                  { label: "speciality", value: doctor.speciality },
                  { label: "Start Date", value: doctor.startDate},
                  { label: "Experience", value: `${doctor.experience}+ Years` },
                  { label: "License Number", value: doctor.licenseNumber },
                  { label: "Mobile", value: doctor.mobile },
                  { label: "Department", value: doctor.department },
                  { label: "Degree", value: doctor.degree },
                ].map((item, index) => (
                  <div className="profile-field" key={index}>
                    <Text strong>{item.label}: </Text>
                    <Text>{item.value}</Text>
                  </div>
                ))}
                <Button 
                  type="primary" 
                  icon={<UserSwitchOutlined />} 
                  onClick={openModal} 
                  style={{ marginTop: "20px" }}>
                  Update Profile
                </Button>
              </Card>
            </Col>

            {/* Hospital Profile Section */}
            <Col xs={24} md={12} style={{ display: 'flex', flexDirection: 'column' }}>
              <Card title={<Title level={3}>Hospital Information</Title>} style={{ flex: 1 }}>
                <Avatar
                  size={240}
                  src={displayImage(hospital?.images?.[0])}
                  icon={!hospital?.images ? <LocalHospital /> : null}
                  shape="square"
                  style={{ marginBottom: "16px", borderRadius: "10px" }}
                />
                {[ 
                  { label: "Hospital Name", value: hospital.hospitalName },
                  { label: "Email", value: hospital.email },
                  { label: "Mobile", value: hospital.mobile },
                  { label: "Website", value: hospital.website },
                  { label: "Established Year", value: hospital.establishedYear },
                  { label: "Overview", value: hospital.overview }
                ].map((item, index) => (
                  <div className="profile-field" key={index}>
                    <Text strong>{item.label}: </Text>
                    <Text>{item.value}</Text>
                  </div>
                ))}
              </Card>
            </Col>
          </Row>
        )}
      </Card>

      {/* Modal for Profile Update */}
      <Modal
        title="Update Doctor Profile"
        open={isModalOpen}
        onCancel={closeModal}
        loading={load}
        footer={null}
        style={{ borderRadius: "8px" }}
      >
        <Form layout="vertical" form={form}>
          <Form.Item label="Doctor Image">
            <Upload
              action={`${URL}/upload-image`}
              listType="picture-card"
              onChange={handleImageChange}
              onRemove={handleRemoveImage}
              accept="image/*"
              maxCount={1}
            >
              <Button icon={<UploadOutlined />}>Upload</Button>
            </Upload>
          </Form.Item>

          {[
            "firstName",
            "lastName",
          ].map((field) => (
            <Form.Item label={generateLabel(field)} key={field}>
              <Input
                name={field}
                value={newData[field]}
                onChange={handleInputChange}
              />
            </Form.Item>
          ))}

          <Form.Item label="Gender">
            <Select
              value={newData.gender}
              onChange={(value) => setNewData((prev) => ({ ...prev, gender: value }))}
            >
              <Option value="MALE">Male</Option>
              <Option value="FEMALE">Female</Option>
              <Option value="OTHER">Other</Option>
            </Select>
          </Form.Item>

          {["email",
            "mobile",
            "startDate",
            "speciality",
            "licenseNumber",
            "department",
            "degree",
          ].map((field) => (
            <Form.Item label={generateLabel(field)} key={field}>
              <Input
                name={field}
                value={newData[field]}
                type={field === 'startDate' ? `date` : 'text'}
                onChange={handleInputChange}
              />
            </Form.Item>
          ))}

          <Button type="primary" icon={<UserSwitchOutlined />} onClick={updateProfile} style={{ width: "100%" }}>
            Update
          </Button>
        </Form>
      </Modal>
    </div>
  );
};

export default DoctorProfile;
