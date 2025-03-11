import axios from "axios";
import { patientURL } from "../../Api & Services/Api.js";

export const fetchPatientProfileData = async (setPatient, setLoading, setError) => {
  setLoading(true);
  try {
    const token = localStorage.getItem("token");
    const response = await axios.get(`${patientURL}/profile`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    setPatient(response.data); 
  } catch (error) {
    setError("Error fetching profile data.");
    console.error("Error fetching profile data", error);
  } finally{
    setLoading(false);
  }
};