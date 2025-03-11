import { Link } from "react-router-dom";
import {Typography, Box, Container, Grid2} from "@mui/material";
import { Card } from "antd";
import { FacebookFilled, TwitterSquareFilled, LinkedinFilled, MailFilled, PhoneFilled, GlobalOutlined } from "@ant-design/icons";

function Footer() {
  return (
    <Card style={{ backgroundColor: "#001529", color: "#fff", borderRadius: 0, boxShadow: "none", padding: "40px 0" }}>
      <Container maxWidth="lg">
        <Grid2 container spacing={4} justifyContent="space-between">
          
          {/* Branding & Tagline */}
          <Grid2 size={{xs: 12, sm: 6, md: 3}}>
            <Typography variant="h4" fontWeight="bold" gutterBottom style={{ fontFamily: "Poppins, sans-serif", color: "#40a9ff" }}>
              AyuMed
            </Typography>
            <Typography variant="body1" color="gray" style={{ fontFamily: "Poppins, sans-serif" }}>
              Empowering your healthcare journey with precision and care.
            </Typography>
          </Grid2>

          {/* Quick Links */}
          <Grid2 size={{xs: 12, sm: 6, md: 2}}>
            <Typography variant="h5" fontWeight="bold" gutterBottom style={{ fontFamily: "Poppins, sans-serif", color: "#40a9ff" }}>
              Quick Links
            </Typography>
            <Box sx={{ display: "flex", flexDirection: "column", gap: 1 }}>
              {["About Us", "Our Services", "Careers", "Contact Us"].map((link, index) => (
                <Link
                  key={index}
                  to={`/${link.toLowerCase().replace(" ", "-")}`}
                  style={{ textDecoration: "none", color: "#d9d9d9", transition: "color 0.3s" }}
                  onMouseOver={(e) => (e.target.style.color = "#40a9ff")}
                  onMouseOut={(e) => (e.target.style.color = "#d9d9d9")}
                >
                  {link}
                </Link>
              ))}
            </Box>
          </Grid2>

          {/* Support */}
          <Grid2 size={{xs: 12, sm: 6, md: 2}}>
            <Typography variant="h5" fontWeight="bold" gutterBottom style={{ fontFamily: "Poppins, sans-serif", color: "#40a9ff" }}>
              Support
            </Typography>
            <Box sx={{ display: "flex", flexDirection: "column", gap: 1 }}>
              {["Help Center", "FAQ", "Privacy Policy", "Terms & Conditions"].map((link, index) => (
                <Link
                  key={index}
                  to={`/${link.toLowerCase().replace(/ /g, "-")}`}
                  style={{ textDecoration: "none", color: "#d9d9d9", transition: "color 0.3s" }}
                  onMouseOver={(e) => (e.target.style.color = "#40a9ff")}
                  onMouseOut={(e) => (e.target.style.color = "#d9d9d9")}
                >
                  {link}
                </Link>
              ))}
            </Box>
          </Grid2>

          {/* Contact Info */}
          <Grid2 size={{xs: 12, sm: 6, md: 3}}>
            <Typography variant="h5" fontWeight="bold" gutterBottom style={{ fontFamily: "Poppins, sans-serif", color: "#40a9ff" }}>
              Contact
            </Typography>
            <Box sx={{ display: "flex", flexDirection: "column", gap: 1 }}>
              <Typography variant="body1" color="gray">
                <MailFilled style={{ color: "#40a9ff" }} />{" "}
                <a href="mailto:support@ayumed.com" style={{ textDecoration: "none", color: "#d9d9d9" }}>
                  support@ayumed.com
                </a>
              </Typography>
              <Typography variant="body1" color="gray">
                <PhoneFilled style={{ color: "#40a9ff" }} />{" "}
                <a href="tel:+02254545252" style={{ textDecoration: "none", color: "#d9d9d9" }}>
                  +022 5454 5252
                </a>
              </Typography>
              <Typography variant="body1" color="gray">
                <GlobalOutlined style={{ color: "#40a9ff" }} />{" "}
                <a href="https://www.ayumed.com" style={{ textDecoration: "none", color: "#d9d9d9" }}>
                  www.AyuMed.com
                </a>
              </Typography>
            </Box>
          </Grid2>
        </Grid2>

        {/* Social Media */}
        <Box sx={{ textAlign: "center", mt: 4 }}>
          <Typography variant="h5" fontWeight="bold" gutterBottom style={{ fontFamily: "Poppins, sans-serif", color: "#40a9ff" }}>
            Follow Us
          </Typography>
          <Box sx={{ display: "flex", justifyContent: "center", gap: 3 }}>
            {[
              { icon: <FacebookFilled />, link: "https://facebook.com", color: "#1877F2" },
              { icon: <TwitterSquareFilled />, link: "https://twitter.com", color: "#1DA1F2" },
              { icon: <LinkedinFilled />, link: "https://linkedin.com", color: "#0077B5" },
            ].map((social, index) => (
              <a key={index} href={social.link} target="_blank" rel="noopener noreferrer">
                <span
                  style={{
                    fontSize: "35px",
                    color: social.color,
                    transition: "transform 0.3s ease",
                  }}
                  onMouseOver={(e) => (e.target.style.transform = "scale(1.1)")}
                  onMouseOut={(e) => (e.target.style.transform = "scale(1)")}
                >
                  {social.icon}
                </span>
              </a>
            ))}
          </Box>
        </Box>

        {/* Footer Bottom */}
        <Box sx={{ mt: 4, textAlign: "center", py: 2, borderTop: "1px solid #404040" }}>
          <Typography variant="body1" color="gray" style={{ fontFamily: "Poppins, sans-serif" }}>
            © 2024 AyuMed. All rights reserved.
          </Typography>
        </Box>
      </Container>
    </Card>
  );
}

export default Footer;
