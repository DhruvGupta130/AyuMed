import { useState } from "react";
import { Input, Button, Form, Card, Typography, message, Row, Col } from "antd";
import { MailOutlined, UserOutlined, MessageOutlined } from "@ant-design/icons";
import "../Styles/Contact.css";

const { Title, Paragraph } = Typography;

const Contact = () => {
  const [form] = Form.useForm();
  const [isSubmitted, setIsSubmitted] = useState(false);

  const handleSubmit = (values) => {
    console.log("Form Data:", values);
    setIsSubmitted(true);
    message.success("Message sent successfully!");
  };

  return (
    <div className="contact-container">
      {isSubmitted && <Confetti />}
      
      <Row justify="center">
        <Col xs={24} sm={18} md={12} lg={10}>
          <Card className="contact-card" bordered={false}>
            <Title level={2} className="contact-title">📩 Contact Us</Title>
            <Paragraph type="secondary" className="contact-description">
              We'd love to hear from you! Fill out the form below or email us at{" "}
              <a href="mailto:support@Ayumed.com">support@Ayumed.com</a>.
            </Paragraph>

            {isSubmitted ? (
              <div className="success-message">
                <Title level={3} className="success-title">🎉 Thank you for reaching out!</Title>
                <Paragraph>We will get back to you as soon as possible.</Paragraph>
                <Button type="primary" size="large" onClick={() => setIsSubmitted(false)}>Send Another Message</Button>
              </div>
            ) : (
              <Form
                form={form}
                layout="vertical"
                onFinish={handleSubmit}
                className="contact-form"
              >
                <Form.Item
                  label="Full Name"
                  name="name"
                  rules={[{ required: true, message: "Please enter your full name" }]}
                >
                  <Input prefix={<UserOutlined />} placeholder="Enter your full name" />
                </Form.Item>

                <Form.Item
                  label="Email Address"
                  name="email"
                  rules={[
                    { required: true, message: "Please enter your email" },
                    { type: "email", message: "Please enter a valid email" },
                  ]}
                >
                  <Input prefix={<MailOutlined />} placeholder="Enter your email address" />
                </Form.Item>

                <Form.Item
                  label="Message"
                  name="message"
                  rules={[{ required: true, message: "Please enter your message" }]}
                >
                  <Input.TextArea
                    prefix={<MessageOutlined />}
                    placeholder="Write your message here"
                    rows={5}
                  />
                </Form.Item>

                <Button type="primary" htmlType="submit" block size="large">
                  Submit
                </Button>
              </Form>
            )}
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default Contact;
