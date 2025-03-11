import { Card, Typography, Carousel, Tooltip } from "antd";
import { HomeOutlined, PhoneOutlined, EnvironmentOutlined } from "@ant-design/icons";
import { displayImage } from "../Api & Services/Api";
import PropTypes from "prop-types";

const { Title, Text } = Typography;

const HospitalCard = ({ hospital, onSelect, selected }) => {

  return (
    <Card
      hoverable
      onClick={onSelect}
      style={{
        width: 320,
        textAlign: "center",
        borderRadius: 12,
        backgroundColor: selected === hospital ? "#B0DFFC" : "#fff",
        cursor: "pointer",
        transition: "0.3s",
        overflow: "hidden",
        boxShadow: "0 4px 10px rgba(0, 0, 0, 0.1)",
      }}
    >
      {/* Image Slideshow or Default Icon */}
      {hospital.images && hospital.images.length > 0 ? (
        <Carousel autoplay effect="fade">
          {hospital.images.map((image, index) => (
            <div key={index}>
              <img
                src={displayImage(image)}
                alt={hospital.hospitalName}
                style={{
                  width: "100%",
                  height: 160,
                  objectFit: "cover",
                  borderRadius: "10px 10px 0 0",
                }}
              />
            </div>
          ))}
        </Carousel>
      ) : (
        <div
          style={{
            width: "100%",
            height: 160,
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
            backgroundColor: "#e6f7ff",
            borderRadius: "10px 10px 0 0",
          }}
        >
          <HomeOutlined style={{ fontSize: 60, color: "#1890ff" }} />
        </div>
      )}

      {/* Hospital Name */}
      <Title level={4} style={{ marginTop: 10, color: "#333", fontWeight: 600 }}>
        {hospital.hospitalName}
      </Title>

      {/* Address with Icon */}
      <Text type="secondary">
        <EnvironmentOutlined style={{ color: "#1890ff", marginRight: 6 }} />
        {hospital.address?.street}, {hospital.address?.city}, {hospital.address?.state}
      </Text>

      {/* Contact Details */}
      <div style={{ marginTop: 10 }}>
        {hospital.phone && (
          <Tooltip title="Call Now">
            <Text style={{ display: "block", color: "#333", fontWeight: 500 }}>
              <PhoneOutlined style={{ color: "#52c41a", marginRight: 6 }} />
              {hospital.phone}
            </Text>
          </Tooltip>
        )}
      </div>
    </Card>
  );
};

HospitalCard.propTypes = {
    hospital: PropTypes.shape({
        hospitalName: PropTypes.string.isRequired,
        images: PropTypes.arrayOf(PropTypes.string),
        address: PropTypes.shape({
            street: PropTypes.string,
            city: PropTypes.string,
            state: PropTypes.string,
        }).isRequired,
        phone: PropTypes.string,
    }).isRequired,
    onSelect: PropTypes.func.isRequired,
    selected: PropTypes.object,
};

export default HospitalCard;
