import "react";
import { Card, Typography, InputNumber, Button, Space } from "antd";
import { 
  MedicineBoxOutlined,
  CalendarOutlined, 
  DropboxOutlined,
  FileTextOutlined 
} from "@ant-design/icons";
import { Add, CurrencyRupeeOutlined, Remove } from "@mui/icons-material";
import PropTypes from "prop-types";

const { Title, Text } = Typography;

const MedicationCard = ({ med, cart, handleQuantityChange }) => {
  const isDisabled = !med.pharmacyStatus;

  return (
    <Card
      hoverable={!isDisabled}
      style={{
        borderRadius: 12,
        padding: 15,
        background: isDisabled ? "#f5f5f5" : "#ffffff",
        display: "flex",
        flexDirection: "column",
        justifyContent: "space-between",
        height: 320,
        minWidth: 280, 
        boxShadow: "0px 4px 12px rgba(0, 0, 0, 0.1)",
        transition: "transform 0.2s ease-in-out",
        opacity: isDisabled ? 0.6 : 1,
        pointerEvents: isDisabled ? "none" : "auto",
      }}
      styles={{
        body: {
          display: "flex", 
          flexDirection: "column",
          height: "100%",
        },
      }}
    >
      {/* 📌 Header: Medication Name & Price */}
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
        <Title level={4} style={{ marginBottom: 5 }}>
          {med.medicationName} <MedicineBoxOutlined style={{ color: "#1890ff" }} />
        </Title>
        <Text strong style={{ fontSize: 22, color: "#52c41a", display: "flex", alignItems: "center" }}>
          <CurrencyRupeeOutlined style={{ fontSize: 18 }} />
          {med.price}
        </Text>
      </div>

      {/* 📌 Pharmacy Status Message */}
      {isDisabled ? (
        <div style={{ textAlign: "center", marginTop: "auto", color: "red", fontWeight: "bold", fontSize: 16 }}>
          🚫 Pharmacy Not Opened
        </div>
      ) : (
        <>
          {/* 📌 Medication Details */}
          <Space direction="vertical" size="small" style={{ flex: 1 }}>
            <Text>
              <FileTextOutlined style={{ color: "#ff4d4f" }} /> Composition: <strong>{med.compositionName}</strong>
            </Text>
            <Text>
              <DropboxOutlined style={{ color: "#faad14" }} /> Dosage: <strong>{med.dosageForm}</strong>
            </Text>
            <Text>
              <CalendarOutlined style={{ color: "#1890ff" }} /> Expiry: <strong>{med.expiry}</strong>
            </Text>
            <Text>
              Stock Available: <strong>{med.quantity}</strong>
            </Text>
          </Space>

          {/* 📌 Quantity Selector & Add Button */}
          <div style={{ marginTop: "auto", textAlign: "center" }}>
            <InputNumber
              min={0}
              max={med.quantity}
              value={cart[med.id] || 0}
              onChange={(value) => handleQuantityChange(med.id, value)}
              style={{ width: "100%", borderRadius: 8 }}
            />

            <div style={{ display: "flex", justifyContent: "space-between", gap: 30 }}>
              <Button
                type="primary"
                icon={<Remove />}
                style={{
                  marginTop: 10,
                  width: "30%",
                  background: "#52c41a",
                  borderColor: "#52c41a",
                }}
                onClick={() => handleQuantityChange(med.id, (cart[med.id] || 0) - 1)}
              />
              <Button
                type="primary"
                icon={<Add />}
                style={{
                  marginTop: 10,
                  width: "30%",
                  background: "#52c41a",
                  borderColor: "#52c41a",
                }}
                onClick={() => handleQuantityChange(med.id, (cart[med.id] || 0) + 1)}
              />
            </div>
          </div>
        </>
      )}
    </Card>
  );
};

MedicationCard.propTypes = {
  med: PropTypes.shape({
    id: PropTypes.number.isRequired,
    medicationName: PropTypes.string.isRequired,
    compositionName: PropTypes.string.isRequired,
    dosageForm: PropTypes.string.isRequired,
    expiry: PropTypes.string.isRequired,
    quantity: PropTypes.number.isRequired,
    price: PropTypes.number.isRequired,
    pharmacyStatus: PropTypes.bool.isRequired,
  }).isRequired,
  cart: PropTypes.objectOf(PropTypes.number).isRequired,
  handleQuantityChange: PropTypes.func.isRequired,
  setBought: PropTypes.func,
};

export default MedicationCard;
