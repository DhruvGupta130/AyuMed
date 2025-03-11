import axios from "axios";
import { doctorURL } from "../../Api & Services/Api.js";

export const fetchDoctorProfileData = async (setManager, setLoading, setError) => {
  setLoading(true);
  try {
    const token = localStorage.getItem("token");
    const response = await axios.get(`${doctorURL}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    setManager(response.data);
  } catch (error) {
    setError("Error fetching profile data!");
    console.error("Error fetching profile data", error);
  } finally{
    setLoading(false);
  }
}

export const fetchDoctorHospital = async (setHospital, setLoading, setError) => {
  setLoading(true);
  try {
    const token = localStorage.getItem("token");
    const response = await axios.get(`${doctorURL}/hospital`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    setHospital(response.data);
  } catch (error) {
    setError("Error fetching hospital data!");
    console.error("Error fetching hospital data", error);
  } finally{
    setLoading(false);
  }
}