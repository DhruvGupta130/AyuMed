import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { CircularProgress, Box, Typography } from "@mui/material";

const Logout = () => {
  const navigate = useNavigate();

  useEffect(() => {
    localStorage.removeItem("token");
    localStorage.setItem("isAuthenticated", "false");
    localStorage.removeItem("role");
    setTimeout(() => {
      navigate("/login");
    }, 500);
  }, [navigate]);

  return (
    <Box
      display="flex"
      justifyContent="center"
      alignItems="center"
      flexDirection="column"
      height="100vh"
      bgcolor="#f4f4f4"
    >
      <CircularProgress size={60} color="primary" /> {/* MUI CircularProgress */}
      <Typography variant="h6" mt={2}>
        Logging out...
      </Typography>
    </Box>
  );
};

export default Logout;
