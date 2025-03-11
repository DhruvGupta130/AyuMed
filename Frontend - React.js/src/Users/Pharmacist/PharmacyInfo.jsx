import { Card, Row, Col, Typography, Button, Carousel, Layout, message, Tag, Divider } from 'antd';
import { displayImage, pharmacyURL } from '../../Api & Services/Api';
import { 
  PhoneOutlined, MailOutlined, GlobalOutlined, HomeOutlined, FieldTimeOutlined, ShopOutlined, 
  MedicineBoxOutlined, InfoCircleOutlined, BankOutlined,
  LaptopOutlined, StarOutlined, EnvironmentOutlined,
  CompassOutlined,
  LockOutlined,
  UnlockOutlined
} from '@ant-design/icons';
import PropTypes from 'prop-types';
import axios from 'axios';
import { convertTo12HourFormat, stringToList } from '../../Api & Services/Services';
import { LocalPharmacyOutlined } from '@mui/icons-material';
import {useState} from "react";

const { Title, Text } = Typography;
const { Content } = Layout;

const PharmacyInfo = ({ Pharmacy, refreshPharmacyData }) => {

  const [loading, setLoading] = useState(false);

  const redirectToMap = () => {
    const { latitude, longitude } = Pharmacy.address;
    window.open(`https://www.google.com/maps?q=${latitude},${longitude}`, '_blank');
  };

  const handleToggleStatus = async () => {
    setLoading(true);
    try {
      const token = localStorage.getItem('token');
      const response = await axios.put(
        `${pharmacyURL}/setStatus`, 
        {}, 
        { headers: { Authorization: `Bearer ${token}` } }
      );
      message.success(response.data.message);
      refreshPharmacyData?.();
    } catch (error) {
      message.error(error?.response?.data?.message || "Unable to update status");
    } finally {
      setLoading(false);
    }
  };

  const isAfter = (openingTime, closingTime) => {
    const now = new Date();
    const [openH, openM, openS] = openingTime.split(":").map(Number);
    const [closeH, closeM, closeS] = closingTime.split(":").map(Number);
    const openingTimeDate = new Date();
    openingTimeDate.setHours(openH, openM, openS, 0);
    const closingTimeDate = new Date();
    closingTimeDate.setHours(closeH, closeM, closeS, 0);
    return now < openingTimeDate && now >= closingTimeDate;
  };

  return (
    <Layout>
      <Content>
        <Row gutter={[32, 32]} justify="center">
          <Col xs={24}>
            <Card
              title={
                <Title level={2} style={{ textAlign: "center", color: "#1d300f", paddingTop: 20, textWrap: 'wrap' }}>
                  {Pharmacy.pharmacyName.toUpperCase()}
                </Title>
              }
            >
              <Row gutter={[24, 24]} style={{ display: 'flex', alignItems: 'stretch' }}>
                {/* Image Carousel */}
                {Pharmacy.images?.length > 0 && (
                  <Col xs={24} md={12}>
                    <Card bordered={false} style={{ borderRadius: "16px", boxShadow: "0 8px 16px rgba(0, 0, 0, 0.1)" }}>
                      <Carousel autoplay effect="fade" style={{ borderRadius: '16px' }}>
                        {Pharmacy.images.map((image, index) => (
                          <div key={index}>
                            <img 
                              src={displayImage(image)} 
                              alt={`Hospital Image ${index + 1}`} 
                              style={{ width: "100%", height: "300px", objectFit: "cover", borderRadius: "16px" }}
                            />
                          </div>
                        ))}
                      </Carousel>
                    </Card>
                  </Col>
                )}

                {/* Pharmacy Details */}
                <Col xs={24} md={12} style={{display: 'flex'}}>
                  <Card bordered={false} style={{ borderRadius: "16px", boxShadow: "0 8px 16px rgba(0, 0, 0, 0.1)", width: '100%' }}
                    title={
                      <Title level={2} style={{ color: '#457b9d', paddingTop: 20, textWrap: 'wrap' }}>
                        <LocalPharmacyOutlined style={{ marginRight: '8px' }} />
                        Pharmacy Information
                      </Title>
                    }>
                    <p style={{fontSize: 17}}><ShopOutlined /> <strong>Name:</strong> {Pharmacy.pharmacyName}</p>
                    <p style={{fontSize: 17}}><PhoneOutlined /> <strong>Mobile:</strong> {Pharmacy.mobile}</p>
                    <p style={{fontSize: 17}}><MailOutlined /> <strong>Email:</strong> {Pharmacy.email}</p>
                    <p style={{fontSize: 17}}><GlobalOutlined /> <strong>Website:</strong> <a href={Pharmacy.website} target="_blank" rel="noopener noreferrer">{Pharmacy.website}</a></p>
                    <p style={{fontSize: 17}}><FieldTimeOutlined /> <strong>Opening Time:</strong> {convertTo12HourFormat(Pharmacy.openingTime)}</p>
                    <p style={{fontSize: 17}}><FieldTimeOutlined /> <strong>Closing Time:</strong> {convertTo12HourFormat(Pharmacy.closingTime)}</p>
                    <Button 
                      type="primary"
                      style={{
                        background: Pharmacy.open ? 'green' : 'red',
                        border: 'none',
                        width: '60%',
                        marginLeft: 5,
                        marginTop: '16px',
                        fontSize: '18px',
                        opacity: isAfter(Pharmacy.openingTime, Pharmacy.closingTime) ? 0.5 : 1,
                        cursor: isAfter(Pharmacy.openingTime, Pharmacy.closingTime) ? 'not-allowed' : 'pointer',
                        color: 'white',
                      }}
                      disabled={isAfter(Pharmacy.openingTime, Pharmacy.closingTime)}
                      onClick={handleToggleStatus}
                      loading={loading}
                    >
                      {Pharmacy.open ? (
                        <span><UnlockOutlined style={{ marginRight: 5 }} /> Opened</span>
                      ) : (
                        <span><LockOutlined style={{ marginRight: 5 }} /> Closed</span>
                      )}

                    </Button>
                  </Card>
                </Col>
              </Row>

              <Divider />

              <Row gutter={[24, 24]} >
                {/* Address Section */}
                <Col xs={24} md={12} lg={7} style={{display: 'flex', padding: '10px 20px' }}>
                  <Card style={{ marginBottom: 20 }} title={
                    <Title level={2} style={{ color: '#457b9d', paddingTop: 20 }}>
                      <HomeOutlined style={{ marginRight: '8px' }} />
                      Address
                    </Title>
                  }>
                    <p style={{fontSize: 17}}><EnvironmentOutlined /> <strong>Street:</strong> {Pharmacy.address.street}</p>
                    <p style={{fontSize: 17}}><BankOutlined /> <strong>City:</strong> {Pharmacy.address.city}</p>
                    <p style={{fontSize: 17}}><CompassOutlined /> <strong>State:</strong> {Pharmacy.address.state}</p>
                    <p style={{fontSize: 17}}><MailOutlined /> <strong>Zip Code:</strong> {Pharmacy.address.zip}</p>

                    <Button type="primary" onClick={redirectToMap} style={{ background: '#457b9d', border: 'none', marginTop: 10 }}>
                      View on Map
                    </Button>
                  </Card>
                </Col>

                {/* Overview */}
                <Col xs={24} md={17}>
                  <Card title={
                    <Title level={2} style={{ color: '#457b9d', paddingTop: 20 }}>
                      <InfoCircleOutlined style={{ marginRight: '8px' }} />
                      Overview
                    </Title>
                  }>
                    <Text style={{ fontSize: '18px' }}>{Pharmacy.overview}</Text>
                  </Card>
                </Col>

              </Row>

              <Divider />

              {/* Services */}
              <Card title={
                <Title level={2} style={{ color: '#457b9d', paddingTop: 20 }}>
                  <MedicineBoxOutlined style={{ marginRight: '8px' }} />
                  Services
                </Title>
              }>
                <ul style={{padding: 20, fontSize: 17}}>
                  {stringToList(Pharmacy.services).map((service, index) => (
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
                  {stringToList(Pharmacy.pharmacyTechnology).map((tech, index) => (
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
                  {stringToList(Pharmacy.accreditations).map((acc, index) => (
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
                {Pharmacy.insurancePartners.map((partner, index) => (
                  <Tag color="blue" key={index} style={{ fontSize: 16, padding: 5, margin: 5 }}>
                    {partner}
                  </Tag>
                ))}
              </Card>
            </Card>
          </Col>
        </Row>
      </Content>
    </Layout>
  );
};

PharmacyInfo.propTypes = {
  Pharmacy: PropTypes.shape({
    pharmacyName: PropTypes.string.isRequired,
    mobile: PropTypes.string.isRequired,
    email: PropTypes.string.isRequired,
    website: PropTypes.string,
    openingTime: PropTypes.string.isRequired,
    closingTime: PropTypes.string.isRequired,
    open: PropTypes.bool.isRequired,
    overview: PropTypes.string.isRequired,
    services: PropTypes.string.isRequired,
    pharmacyTechnology: PropTypes.string.isRequired,
    accreditations: PropTypes.string.isRequired,
    insurancePartners: PropTypes.arrayOf(PropTypes.string).isRequired,
    images: PropTypes.arrayOf(PropTypes.string),
    address: PropTypes.shape({
      street: PropTypes.string.isRequired,
      city: PropTypes.string.isRequired,
      state: PropTypes.string.isRequired,
      zip: PropTypes.string.isRequired,
      latitude: PropTypes.number.isRequired,
      longitude: PropTypes.number.isRequired,
    }).isRequired,
  }).isRequired,
  refreshPharmacyData: PropTypes.func,
};


export default PharmacyInfo;
