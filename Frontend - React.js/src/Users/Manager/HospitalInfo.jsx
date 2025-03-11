import { Card, Row, Col, Typography, Button, Carousel, Layout, Tag, Divider } from 'antd';
import { displayImage } from "../../Api & Services/Api.js";
import { 
  PhoneOutlined, MailOutlined, GlobalOutlined, HomeOutlined, FieldTimeOutlined, ShopOutlined, 
  SafetyCertificateOutlined, HeartOutlined, HddOutlined, EnvironmentOutlined, BankOutlined, 
  CompassOutlined, CheckCircleOutlined, LaptopOutlined, StarOutlined, MedicineBoxOutlined, InfoCircleOutlined
} from '@ant-design/icons';
import { stringToList } from '../../Api & Services/Services.js';
import HospitalDepartmentsCard from '../../Components/HospitalDepartmentsCard.jsx';
import PropTypes from "prop-types";

const { Title, Text } = Typography;
const { Content } = Layout;

const HospitalInfo = ({ hospital }) => {
  const redirectToMap = () => {
    const { latitude, longitude } = hospital.address;
    window.open(`https://www.google.com/maps?q=${latitude},${longitude}`, '_blank');
  };

  return (
    <Layout>
      <Content>
        <Row gutter={[32, 32]} justify="center">
          <Col xs={24}>
            <Card
              title={
                <Title style={{ fontSize: 33, color: '#1d300f', textAlign: 'center', textWrap: 'wrap', paddingTop: 20 }}>
                  {hospital.hospitalName.toUpperCase()}
                </Title>
              }
            >
              {/* Image Carousel */}
              {hospital.images && hospital.images.length > 0 && (
                <Row gutter={[24, 24]} >
                  <Col xs={24} md={12} >
                    <Card bordered={false} style={{ borderRadius: "16px", boxShadow: "0 8px 16px rgba(0, 0, 0, 0.1)" }}>
                      <Carousel autoplay effect="fade">
                        {hospital.images.map((image, index) => (
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

                  {/* Hospital Details */}
                  <Col xs={24} md={12} style={{display: 'flex'}}>
                    <Card bordered={false} style={{ borderRadius: "16px", boxShadow: "0 8px 16px rgba(0, 0, 0, 0.1)", width: '100%' }}>
                      <Title level={2} style={{ color: "#457b9d" }}>
                        <ShopOutlined style={{ marginRight: "8px" }} />
                        Hospital Information
                      </Title>

                      <p style={{fontSize: 17}}><HomeOutlined /> <strong>Name:</strong> {hospital.hospitalName}</p>
                      <p style={{fontSize: 17}}><PhoneOutlined /> <strong>Mobile:</strong> {hospital.mobile}</p>
                      <p style={{fontSize: 17}}><MailOutlined /> <strong>Email:</strong> {hospital.email}</p>
                      <p style={{fontSize: 17}}><GlobalOutlined /> <strong>Website:</strong> <a href={hospital.website} target="_blank" rel="noopener noreferrer">{hospital.website}</a></p>
                      <p style={{fontSize: 17}}><FieldTimeOutlined /> <strong>Established:</strong> {hospital.establishedYear}</p>
                      <p style={{fontSize: 17}}><SafetyCertificateOutlined /> <strong>Emergency Services:</strong> {hospital.emergencyServices ? "Available" : "Not Available"}</p>
                      <p style={{fontSize: 17}}><HeartOutlined /> <strong>ICU Capacity:</strong> {hospital.icuCapacity}</p>
                      <p style={{fontSize: 17}}><HddOutlined /> <strong>Bed Capacity:</strong> {hospital.bedCapacity}</p>
                      <p style={{fontSize: 17}}><ShopOutlined /> <strong>Operation Theaters:</strong> {hospital.operationTheaters}</p>
                    </Card>
                  </Col>
                </Row>
              )}

              <Divider />

              <Row gutter={[24, 24]} >
                {/* Address Section */}
                <Col xs={24} md={12} lg={7} style={{display: 'flex', padding: 30}}>
                  <Card style={{ marginBottom: 20 }} title={
                    <Title level={2} style={{ color: '#457b9d', paddingTop: 20 }}>
                      <HomeOutlined style={{ marginRight: '8px' }} />
                      Address
                    </Title>
                  }> 
                    <p style={{fontSize: 17}}><EnvironmentOutlined /> <strong>Street:</strong> {hospital.address.street}</p>
                    <p style={{fontSize: 17}}><BankOutlined /> <strong>City:</strong> {hospital.address.city}</p>
                    <p style={{fontSize: 17}}><CompassOutlined /> <strong>State:</strong> {hospital.address.state}</p>
                    <p style={{fontSize: 17}}><MailOutlined /> <strong>Zip Code:</strong> {hospital.address.zip}</p>

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
                    <Text style={{ fontSize: '18px' }}>{hospital.overview}</Text>
                  </Card>
                </Col>
                
              </Row>

              <Divider />

              <Card style={{ marginBottom: 20 }} title={
                  <Title level={2} style={{ color: '#457b9d', paddingTop: 20 }}>
                    <HomeOutlined style={{ marginRight: '8px' }} />
                    Departments
                  </Title>
              }> 
                <HospitalDepartmentsCard departments={hospital.departments} />
              </Card>

              <Divider />

              {/* Specialties */}
              <Card title={
                <Title level={2} style={{ color: '#457b9d', paddingTop: 20 }}>
                  <MedicineBoxOutlined style={{ marginRight: '8px' }} />
                  Specialties
                </Title>
              }>
                {hospital.specialities.map((specialty, index) => (
                  <Tag color="green" key={index} style={{ fontSize: 16, padding: 5, margin: 5 }}>
                    <CheckCircleOutlined /> {specialty}
                  </Tag>
                ))}
              </Card>

              <Divider />

              {/* Hospital Technology */}
              <Card title={
                <Title level={2} style={{ color: '#457b9d', paddingTop: 20 }}>
                  <LaptopOutlined style={{ marginRight: '8px' }} />
                  Hospital Technology
                </Title>
              }>
                <ul style={{padding: 20, fontSize: 17}}>
                  {stringToList(hospital.technology).map((tech, index) => (
                    <li key={index}>
                      <strong>{tech.heading}:</strong> {tech.descriptionText}
                    </li>
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
                  {stringToList(hospital.accreditations).map((acc, index) => (
                    <li key={index}>
                      <strong>{acc.heading}:</strong> {acc.descriptionText}
                    </li>
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
                {hospital.insurancePartners.map((partner, index) => (
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

HospitalInfo.propTypes = {
  hospital: PropTypes.shape({
    hospitalName: PropTypes.string.isRequired,
    mobile: PropTypes.string.isRequired,
    email: PropTypes.string.isRequired,
    website: PropTypes.string,
    establishedYear: PropTypes.number.isRequired,
    emergencyServices: PropTypes.bool,
    icuCapacity: PropTypes.number,
    bedCapacity: PropTypes.number,
    operationTheaters: PropTypes.number,
    overview: PropTypes.string,
    images: PropTypes.arrayOf(PropTypes.string),
    specialities: PropTypes.arrayOf(PropTypes.string),
    technology: PropTypes.string,
    accreditations: PropTypes.string,
    insurancePartners: PropTypes.arrayOf(PropTypes.string),
    address: PropTypes.shape({
      street: PropTypes.string.isRequired,
      city: PropTypes.string.isRequired,
      state: PropTypes.string.isRequired,
      zip: PropTypes.string.isRequired,
      latitude: PropTypes.number.isRequired,
      longitude: PropTypes.number.isRequired,
    }).isRequired,
    departments: PropTypes.arrayOf(PropTypes.string),
  }).isRequired,
};


export default HospitalInfo;
