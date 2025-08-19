import { useEffect, useState } from "react";
import { Card, Col, Row, Typography, Spin, Divider, Avatar } from "antd";
import { StarFilled, UserOutlined } from "@ant-design/icons";
import axios from "axios";
import { doctorURL } from "../../Api & Services/Api";
import { StarBorder } from "@mui/icons-material";

const { Text } = Typography;

const FeedbackPage = () => {
  const [feedbacks, setFeedbacks] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchFeedbacks = async () => {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.get(`${doctorURL}/feedback`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        setFeedbacks(response.data);
      } catch (error) {
        console.error("Error fetching feedbacks:", error);
        alert("Failed to fetch feedbacks. Please try again.");
      } finally {
        setLoading(false);
      }
    };

    fetchFeedbacks();
  }, []);

  return (
    <div className="profile-container" style={{ padding: "20px" }}>
      <Card
        title={
          <Typography.Title level={3} style={{ fontWeight: "500" }}>
            Patient Feedbacks
          </Typography.Title>
        }
        style={{ borderRadius: "12px", boxShadow: "0 4px 12px rgba(0, 0, 0, 0.1)" }}
      >
        {loading ? (
            <Spin tip="Loading Feedbacks..." size="large" style={{ display: "block", margin: "auto" }} />
        ) : (
          <Row gutter={[16, 16]}>
            {feedbacks.length > 0 ? feedbacks.map((feedback) => (
              <Col xs={24} sm={12} md={8} lg={6} key={feedback.id}>
                <Card
                  hoverable
                  bordered={false}
                  style={{
                    width: "100%",
                    borderRadius: "12px",
                    boxShadow: "0 2px 6px rgba(0,0,0,0.1)",
                    backgroundColor: "#ffffff",
                    transition: "all 0.3s ease",
                  }}
                >
                    <div style={{ display: "flex", justifyContent: "center" }}>
                      <Avatar
                        size={100}
                        src={feedback.patient.image}
                        icon={<UserOutlined />}
                        style={{
                          border: "2px solid #fadb14",
                          backgroundColor: "#f0f0f0",
                        }}
                      />
                    </div>
                  {/* Patient's Name */}
                  <div style={{textAlign: 'center', margin: 0}}>
                    <Text strong style={{ fontSize: "16px", color: "#333" }}>
                        {feedback.patient ? feedback.patient.fullName : "Anonymous"}
                    </Text>
                    <Divider style={{ margin: "10px 0" }} />
                  </div>

                  {/* Rating with Stars */}
                  <div style={{ display: "flex", alignItems: "center", marginBottom: "10px", justifyContent: 'center' }}>
                    {[...Array(5)].map((_, index) => (
                      index < feedback.rating ? (
                        <StarFilled key={index} style={{ color: "#fadb14", fontSize: "18px", marginRight: "5px" }} />
                      ) : (
                        <StarBorder key={index} style={{ color: "#fadb14", fontSize: "18px", marginRight: "5px" }} />
                      )
                    ))}
                  </div>

                  {/* Feedback Comments */}
                  <Text style={{ fontSize: "14px", color: "#555", display: "block", marginBottom: "15px" }}>
                    {feedback.comments}
                  </Text>
                </Card>
              </Col>
            )): "No feedbacks available"}
          </Row>
        )}
      </Card>
    </div>
  );
};

export default FeedbackPage;
