import { useEffect, useState } from "react";
import MedicalRecordsCard from "./MedicalRecordsCard.jsx";
import { Alert, CircularProgress } from "@mui/material";
import axios from "axios";
import { patientURL } from "../../Api & Services/Api.js";

const MedicalRecords = () => {
  const [histories, setHistories] = useState([]);  
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchMedicalHistories = async () => {
    setLoading(true);
    try {
      const token = localStorage.getItem("token");
      const response = await axios.get(`${patientURL}/medical-histories`,{
        headers:{
          Authorization: `Bearer ${token}`
        }
      });
      setHistories(response.data);
      console.log(response.data);
    } catch(error) {
      setError("Failed to fetch Medical Histories");
      console.error(error);
    } finally{
      setLoading(false);
    }
  }

  useEffect(() => {
    fetchMedicalHistories();
  }, []);

  if (loading) {
    return <div className="loader"><CircularProgress/></div>;
  }

  if (error) {
    return (
      <Alert sx={{ display: "flex", justifyContent: "center", fontSize: "medium" }} severity="error">
        {error}
      </Alert>
      );
  }

  return (
    <div className='medical-history'>
      <div className="dashboard">
          {histories.length>0 ?(<div className="medical-list">
            {histories.map((history, index) => (
              <MedicalRecordsCard
                key={index}
                medicalHistories={[history]}
                medications={history.medications}
              />
            ))}
          </div>):(
            <div className="info-message">
              <Alert severity="info" icon={false}>No Medical records available right now.</Alert>
            </div>
          )
        }
      </div>
    </div>
  );
};

export default MedicalRecords;
