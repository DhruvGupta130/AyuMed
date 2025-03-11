import React, { useEffect } from "react";
import { Card, CardContent, CardMedia, Typography, Box, Divider } from "@mui/material";
import { Link } from "react-router-dom";
import PropTypes from "prop-types";
import MedicationIcon from "@mui/icons-material/Medication";
import LocalHospitalIcon from "@mui/icons-material/LocalHospital";
import PersonIcon from "@mui/icons-material/Person";
import { LocalPharmacy } from "@mui/icons-material";
import { displayImage } from "../Api & Services/Api.js";
import { convertTo12HourFormat } from "../Api & Services/Services.js";

const getFormattedDetails = (value) => (value ? value : "N/A");

function SearchResultCard({ result, type }) {
  const linkTo = `/page/profile/${type.toLowerCase()}/${result.id}`;
  
  const images = (() => {
    switch (type) {
      case "Hospital":
      case "Pharmacy":
        return result?.images || [];
      case "Doctor":
        return result?.image ? [result?.image] : [];
      case "Medication":
        return [];
      default:
        return [];
    }
  })();

  const doctorName = (name) => {
    return `Dr. ${name}`;
  }

  const [imageIndex, setImageIndex] = React.useState(0);

  useEffect(() => {
    if (images.length > 1) {
      const intervalId = setInterval(() => {
        setImageIndex((prevIndex) => (prevIndex + 1) % images.length);
      }, 3000); 

      return () => clearInterval(intervalId);
    }
  }, [images.length]);

  const imageSrc = images[imageIndex] ? displayImage(images[imageIndex]) : null;

  const title = (() => {
    switch (type) {
      case "Hospital":
        return result.hospitalName || "N/A";
      case "Pharmacy":
        return result.pharmacyName || "N/A";
      case "Doctor":
        return doctorName(result.fullName) || "N/A";
      case "Medication":
        return result.medicationName || "N/A";
      default:
        return "N/A";
    }
  })();

  const description = (() => {
    switch (type) {
      case "Hospital":
        return result.overview || "No additional details available.";
      case "Pharmacy":
        return result.overview || "No additional details available.";
      case "Doctor":
        return result.speciality || "No additional details available.";
      case "Medication":
        return result.compositionName + " " + result.strength + " " + result.dosageForm || "No additional details available.";
      default:
        return "No additional details available.";
    }
  })();

  const additionalDetails = (() => {
    if (type === "Hospital") {
      return {
        Address: `${getFormattedDetails(result?.address?.street)}, ${getFormattedDetails(result?.address?.city)}, ${getFormattedDetails(result?.address?.state)}`,
        Established: getFormattedDetails(result.establishedYear),
        Mobile: getFormattedDetails(result.mobile),
        Email: getFormattedDetails(result.email),
        Website: getFormattedDetails(result.website),
      };
    } else if (type === "Pharmacy") {
      return {
        Address: `${getFormattedDetails(result?.address?.street)}, ${getFormattedDetails(result?.address?.city)}, ${getFormattedDetails(result?.address?.state)}`,
        Mobile: getFormattedDetails(result.mobile),
        Email: getFormattedDetails(result.email),
        Website: getFormattedDetails(result.website),
        OpeningTime: getFormattedDetails(convertTo12HourFormat(result.openingTime)),  
        ClosingTime: getFormattedDetails(convertTo12HourFormat(result.closingTime)),
      };
    } else if (type === "Doctor") {
      return {
        Experience: `${getFormattedDetails(result.experience)} years`,
        Degree: getFormattedDetails(result.degree),
        Department: getFormattedDetails(result.department),
      };
    } else if (type === "Medication") {
      return {
        Manufacturer: getFormattedDetails(result.manufacturer),
        Price: getFormattedDetails(result.price),
        Expiry: getFormattedDetails(result.expiry),
      };
    }
    return null;
  })();

  const renderIcon = () => {
    switch (type) {
      case "Hospital":
        return <LocalHospitalIcon sx={{ fontSize: "100px", color: "#888" }} />;
      case "Pharmacy":
        return <LocalPharmacy sx={{ fontSize: "100px", color: "#888" }} />;
      case "Doctor":
        return <PersonIcon sx={{ fontSize: "100px", color: "#888" }} />;
      case "Medication":
        return <MedicationIcon sx={{ fontSize: "100px", color: "#888" }} />;
      default:
        return <Box sx={{ fontSize: "100px", color: "#888" }}>Unknown</Box>;
    }
  };

  return (
    <Card
      className="search-result-card"
      sx={{
        width: 300,
        height: "auto",
        display: "flex",
        flexDirection: "column",
        borderRadius: 4,
        overflow: "hidden",
        transition: "transform 0.3s ease, box-shadow 0.3s ease",
        "&:hover": {
          transform: "translateY(-8px)",
          boxShadow: "0 8px 20px rgba(0, 0, 0, 0.2)",
        },
      }}
    >
      <Link to={linkTo} className="card-link" style={{ textDecoration: "none", color: "inherit" }}>
        <Box
          sx={{
            width: "100%",
            height: 200,
            backgroundColor: "#f0f0f0",
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
            position: "relative",
          }}
        >
          {imageSrc ? (
            <CardMedia
              component="img"
              alt={title}
              image={imageSrc}
              sx={{
                maxWidth: "100%",
                maxHeight: "100%",
                objectFit: "cover",
              }}
            />
          ) : (
            renderIcon()
          )}
        </Box>

        <CardContent sx={{ padding: 2 }}>
          <Typography
            variant="h6"
            component="div"
            sx={{
              fontWeight: 600,
              color: "text.primary",
              overflow: "hidden",
              textOverflow: "ellipsis",
              whiteSpace: "nowrap",
            }}
          >
            {title}
          </Typography>
          <Typography
            variant="body2"
            color="text.secondary"
            sx={{
              marginTop: 1,
              overflow: "hidden",
              textOverflow: "ellipsis",
              display: "-webkit-box",
              WebkitBoxOrient: "vertical",
              WebkitLineClamp: 2,
            }}
          >
            {description}
          </Typography>
          <Divider sx={{ my: 2 }} />
          {additionalDetails && (
            <Box sx={{ display: "flex", flexDirection: "column", gap: 1 }}>
              {Object.entries(additionalDetails).map(([key, value]) => (
                <Typography
                  key={key}
                  variant="body2"
                  sx={{
                    color: "text.secondary",
                    fontSize: "0.85rem",
                    whiteSpace: "nowrap",
                    overflow: "hidden",
                    textOverflow: "ellipsis",
                  }}
                >
                  <strong>{key}:</strong> {value}
                </Typography>
              ))}
            </Box>
          )}
        </CardContent>
      </Link>
    </Card>
  );
}

SearchResultCard.propTypes = {
  result: PropTypes.shape({
    id: PropTypes.number.isRequired,
    image: PropTypes.string,
    images: PropTypes.array,
    hospitalName: PropTypes.string,
    pharmacyName: PropTypes.string,
    overview: PropTypes.string,
    medicationName: PropTypes.string,
    compositionName: PropTypes.string,
    strength: PropTypes.string,
    dosageForm: PropTypes.string,
    expiry: PropTypes.string,
    openingTime: PropTypes.string,
    closingTime: PropTypes.string,
    firstName: PropTypes.string,
    lastName: PropTypes.string,
    fullName: PropTypes.string,
    name: PropTypes.string,
    description: PropTypes.string,
    speciality: PropTypes.string,
    address: PropTypes.shape({
      street: PropTypes.string,
      city: PropTypes.string,
      state: PropTypes.string,
    }),
    establishedYear: PropTypes.number,
    mobile: PropTypes.string,
    email: PropTypes.string,
    website: PropTypes.string,
    experience: PropTypes.number,
    degree: PropTypes.string,
    department: PropTypes.string,
    manufacturer: PropTypes.string,
    price: PropTypes.number,
    details: PropTypes.string,
  }).isRequired,
  type: PropTypes.string.isRequired
};

export default SearchResultCard;
