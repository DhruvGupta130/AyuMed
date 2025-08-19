import  {useState, useEffect, useCallback} from "react";
import { useNavigate, useParams } from "react-router-dom";
import axios from "axios";
import { Card, Row, Col, Typography, Button, Carousel, Layout, Tag, Divider, Space, Spin } from "antd";
import { 
  PhoneOutlined, MailOutlined, GlobalOutlined, HomeOutlined, MedicineBoxOutlined, BankOutlined, FieldTimeOutlined,
  UnlockOutlined,
  LockOutlined,
  DashboardOutlined,
  InfoCircleOutlined,
  StarOutlined,
  LaptopOutlined,
  HeartTwoTone,
  ShoppingCartOutlined
} from "@ant-design/icons";
import { patientURL } from "../../Api & Services/Api.js";
import { stringToList } from "../../Api & Services/Services.js";
import { LocalPharmacyOutlined } from "@mui/icons-material";

const { Title, Text } = Typography;
const { Content } = Layout;

const PharmacyProfilePage = () => {
  const { id } = useParams();
  const [pharmacy, setPharmacy] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [medications, setMedications] = useState([]);

  const navigate = useNavigate();

    const fetchPharmacy = useCallback(async () => {
        setLoading(true);
        try {
            const token = localStorage.getItem("token");
            const response = await axios.get(`${patientURL}/pharmacy/${id}`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            setPharmacy(response.data);
        } catch (err) {
            setError(err.response?.data?.message || "Error fetching pharmacy details.");
            console.error(err);
        } finally {
            setLoading(false);
        }
    }, [id]);

    const fetchMedications = useCallback(async () => {
        setLoading(true);
        try {
            const token = localStorage.getItem("token");
            const response = await axios.get(`${patientURL}/${id}/medications`,{
                headers:{
                    Authorization: `Bearer ${token}`
                },
            });
            setMedications(response.data);
        } catch (err) {
            setError(err.response?.data?.message || "Error fetching medications details.");
            console.error(err);
        } finally {
            setLoading(false);
        }
    }, [id]);
    
  useEffect(() => {
    fetchPharmacy();
  }, [fetchPharmacy]);

  useEffect(() => {
    if(pharmacy) {
        fetchMedications();
    }
  }, [fetchMedications, pharmacy]);

  const convertTo12HourFormat = (time) => {
    if (!time) return "Not Available";
    const [hours, minutes] = time.split(":");
    const period = hours >= 12 ? "PM" : "AM";
    const formattedHours = hours % 12 || 12;
    return `${formattedHours}:${minutes} ${period}`;
  };

  const redirectToMap = () => {
    if (!pharmacy.address?.latitude || !pharmacy.address?.longitude) return;
    window.open(`https://www.google.com/maps?q=${pharmacy.address.latitude},${pharmacy.address.longitude}`, "_blank");
  };

  if (loading) return <Spin size="large" style={{ display: "block", margin: "auto", marginTop: "20%" }} />;
  if (error) return <p style={{ textAlign: "center", fontSize: 18, color: "red" }}>{error}</p>;

  return (
    <Layout>
      <Content>
        <Row gutter={[24, 24]} justify="center" style={{ gap: 20, padding: 20 }}>
          <Col span={24}>
            <Card 
              title={<Title level={2} style={{ textAlign: "center", color: "#1d300f", paddingTop: 20, textWrap: 'wrap', fontWeight: 700 }}>
              {pharmacy.pharmacyName.toUpperCase()}
            </Title>}
              bordered={false}
              style={{ boxShadow: "0px 4px 12px rgba(0, 0, 0, 0.1)", borderRadius: 10 }}
            >
              
              {/* Pharmacy Image Carousel & Details */}
              <Row gutter={[24, 24]}>
                {pharmacy.images?.length > 0 && (
                  <Col xs={24} md={12} style={{ display: "flex" }}>
                    <Card style={{ width: "100%", boxShadow: "0px 2px 8px rgba(0, 0, 0, 0.15)" }}>
                      <Carousel autoplay effect="fade" style={{ borderRadius: 8 }}>
                        {pharmacy.images.map((image, index) => (
                          <div key={index} style={{ height: 250 }}>
                                <img src={image} alt="Pharmacy" style={{ width: "100%", borderRadius: 8, objectFit: "cover", height: 380 }} />
                          </div>
                        ))}
                      </Carousel>
                    </Card>
                  </Col>
                )}

                {/* Pharmacy Details */}
                <Col xs={24} md={12} style={{ display: "flex" }}>
                  <Card style={{ flex: 1, display: "flex", flexDirection: "column"}}
                    title={
                      <Title level={2} style={{ color: '#457b9d', paddingTop: 20, textWrap: 'wrap' }}>
                        <LocalPharmacyOutlined style={{ marginRight: '8px' }} />
                        Pharmacy Information
                      </Title>
                    }>
                        <Space direction="vertical" size="middle" style={{padding: 0, margin: 0}} >
                            <Text style={{ fontSize: 18 }}><HomeOutlined /> {pharmacy.address.street}, {pharmacy.address.city}, {pharmacy.address.state}, {pharmacy.address.zip}</Text>
                            <Text style={{ fontSize: 18 }}><PhoneOutlined /> {pharmacy.mobile}</Text>
                            <Text style={{ fontSize: 18 }}><MailOutlined /> {pharmacy.email}</Text>
                            <Text style={{ fontSize: 18 }}><GlobalOutlined /> <a href={pharmacy.website} target="_blank" rel="noopener noreferrer">{pharmacy.website}</a></Text>
                            <Text style={{fontSize: 18}}><FieldTimeOutlined /> <strong>Opening Time:</strong> {convertTo12HourFormat(pharmacy.openingTime)}</Text>
                            <Text style={{fontSize: 18}}><FieldTimeOutlined /> <strong>Closing Time:</strong> {convertTo12HourFormat(pharmacy.closingTime)}</Text>
                            <div style={{display: 'flex', alignItems: 'center', justifyItems:'space-between', gap: 10}}>
                                <strong style={{fontSize: 17}}>
                                    <DashboardOutlined/> Status: 
                                </strong>
                                <p style={{
                                    color: pharmacy.open ? "green" : "red",
                                    fontSize: 18,
                                    fontWeight: 700,
                                    textAlign: "center"
                                }}>
                                {pharmacy.open ? (
                                    <span><UnlockOutlined style={{ marginRight: 5 }} /> Opened</span>
                                ) : (
                                    <span><LockOutlined style={{ marginRight: 5 }} /> Closed</span>
                                )}

                                </p>
                            </div>
                            <Button type="primary" onClick={redirectToMap}>View on Map</Button>
                        </Space>
                  </Card>
                </Col>
              </Row>

              <Divider />

              {/* Overview */}
              <Card title={
                <Title level={2} style={{ color: '#457b9d', paddingTop: 20 }}>
                    <InfoCircleOutlined style={{ marginRight: '8px' }} />
                    Overview
                </Title>
              }>
                <Text style={{ fontSize: '18px' }}>{pharmacy.overview || "No overview available."}</Text>
              </Card>

              <Divider />

              {/* Services */}
              <Card title={
                <Title level={2} style={{ color: '#457b9d', paddingTop: 20 }}>
                  <HeartTwoTone style={{ marginRight: '8px' }} />
                  Services
                </Title>
              }>
                <ul style={{padding: 20, fontSize: 17}}>
                  {stringToList(pharmacy.services).map((service, index) => (
                    <li key={index}><strong>{service.heading}:</strong> {service.descriptionText}</li>
                  ))}
                </ul>
              </Card>

              <Divider />

              {/* Pharmacy Technology */}
              <Card title={
                <Title level={2} style={{ color: '#457b9d', paddingTop: 20 }}>
                  <LaptopOutlined style={{ marginRight: '8px' }} />
                  Pharmacy Technology
                </Title>
              }>
                <ul style={{padding: 20, fontSize: 17}}>
                  {stringToList(pharmacy.pharmacyTechnology).map((tech, index) => (
                    <li key={index}><strong>{tech.heading}:</strong> {tech.descriptionText}</li>
                  ))}
                </ul>
              </Card>

              <Divider />

              {/* Accreditations */}
              <Card title={
                <Title level={2} style={{ color: '#457b9d', paddingTop: 20 }}>
                  <StarOutlined style={{ marginRight: '8px' }} />
                  Accreditations
                </Title>
              }>
                <ul style={{padding: 20, fontSize: 17}}>
                  {stringToList(pharmacy.accreditations).map((acc, index) => (
                    <li key={index}><strong>{acc.heading}:</strong> {acc.descriptionText}</li>
                  ))}
                </ul>
              </Card>

              <Divider />

              {/* Insurance Partners */}
              <Card title={
                <Title level={2} style={{ color: '#457b9d', paddingTop: 20 }}>
                  <BankOutlined style={{ marginRight: '8px' }} />
                  Insurance Partners
                </Title>
              }>
                {pharmacy.insurancePartners.map((partner, index) => (
                  <Tag color="blue" key={index} style={{ fontSize: 16, padding: 5, margin: 5 }}>
                    {partner}
                  </Tag>
                ))}
              </Card>

              <Divider />

              {/* Medications Available */}
                <Card 
                title={
                    <Title level={2} style={{ color: '#457b9d', paddingTop: 20 }}>
                    <MedicineBoxOutlined style={{ marginRight: '8px' }} />
                    Available Medicines
                    </Title>
                }
                bordered={false}
                style={{ borderRadius: 10, boxShadow: "0px 4px 12px rgba(0, 0, 0, 0.1)", marginTop: 20 }}
                >
                {medications?.length > 0 ? (
                  <Row gutter={[16, 16]} justify="start">
                    {medications.map((med, index) => (
                      <Col xs={24} sm={12} md={8} lg={6} key={index}>
                        <Card 
                          bordered 
                          hoverable={pharmacy.open}
                          style={{
                            borderRadius: 12, 
                            textAlign: "center", 
                            backgroundColor: "#ffffff", 
                            boxShadow: "0 2px 8px rgba(0, 0, 0, 0.1)",
                            transition: "transform 0.2s",
                            opacity: pharmacy.open ? 1 : 0.6, // Reduce opacity if pharmacy is closed
                            pointerEvents: pharmacy.open ? "auto" : "none", // Disable interactions if closed
                          }}
                          onClick={() => pharmacy.open && navigate(`/page/order-medicines?search=${med.medicationName}`)}
                          onMouseEnter={(e) => pharmacy.open && (e.currentTarget.style.transform = "scale(1.04)")}
                          onMouseLeave={(e) => pharmacy.open && (e.currentTarget.style.transform = "scale(1.00)")}
                        >
                          <MedicineBoxOutlined style={{ fontSize: 24, color: "#52c41a", marginBottom: 10 }} />
                          <Title level={4} style={{ marginBottom: 5 }}>{med.medicationName}</Title>
                          
                          <Tag color="blue" style={{ fontSize: 14, padding: "5px 10px", marginBottom: 8 }}>
                            {med.dosageForm} | {med.strength}
                          </Tag>

                          <Text type="secondary" style={{ fontSize: 16, display: "block", marginBottom: 10 }}>
                            {med.compositionName} | ₹{med.price}
                          </Text>

                          {pharmacy.open ? (
                            <Button 
                              type="primary" 
                              icon={<ShoppingCartOutlined />} 
                              size="large"
                              style={{ width: "100%", borderRadius: 6, backgroundColor: 'green' }}
                              onClick={(e) => {
                                e.stopPropagation();
                                navigate(`/page/order-medicines?search=${med.medicationName}`);
                              }}
                            >
                              Buy Now
                            </Button>
                          ) : (
                            <Text type="danger" style={{ fontSize: 16, fontWeight: "bold" }}>
                              🚫 Pharmacy Not Opened
                            </Text>
                          )}
                        </Card>
                      </Col>
                    ))}
                  </Row>
                ) : (
                  <Title level={4} style={{ textAlign: "center", color: "#ff4d4f" }}>
                    No medications found.
                  </Title>
                )}
                </Card>

              <Divider />

            </Card>
          </Col>
        </Row>
      </Content>
    </Layout>
  );
};

export default PharmacyProfilePage;
