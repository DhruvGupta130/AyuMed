import "react";
import { Card, List, Typography, Button } from "antd";
import { DeleteOutlined } from "@ant-design/icons";
import PropTypes from "prop-types";

const { Title, Text } = Typography;

const CartSummary = ({ cart, medications, handleQuantityChange }) => {
  const cartItems = medications.filter((med) => cart[med.id] > 0);

  const totalCost = cartItems.reduce((acc, med) => acc + med.price * cart[med.id], 0);

  return (
    <Card title="🛒 Your Cart" style={{ borderRadius: 12, marginTop: 20 }}>
      {cartItems.length === 0 ? (
        <Text type="secondary">Your cart is empty.</Text>
      ) : (
        <>
          <List
            dataSource={cartItems}
            renderItem={(med) => (
              <List.Item key={med.id}
                actions={[
                  <Button key={med.id}
                    danger
                    size="small"
                    icon={<DeleteOutlined />}
                    onClick={() => handleQuantityChange(med.id, 0)}
                  />,
                ]}
              >
                <Text>{med.medicationName} (x{cart[med.id]})</Text>
                <Text strong>₹{(med.price * cart[med.id]).toFixed(2)}</Text>
              </List.Item>
            )}
          />
          <Title level={4} style={{ textAlign: "right", marginTop: 10 }}>
            Total: ₹{totalCost.toFixed(2)}
          </Title>
        </>
      )}
    </Card>
  );
};

CartSummary.propTypes = {
  cart: PropTypes.objectOf(PropTypes.number).isRequired, // Cart object with medication IDs as keys and quantity as values
  medications: PropTypes.arrayOf(
      PropTypes.shape({
        id: PropTypes.number.isRequired,
        medicationName: PropTypes.string.isRequired,
        price: PropTypes.number.isRequired,
      })
  ).isRequired,
  handleQuantityChange: PropTypes.func.isRequired, // Function to modify cart quantity
};

export default CartSummary;
