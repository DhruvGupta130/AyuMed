import axios from "axios";
import { hospitalURL } from "../../Api & Services/Api.js";

export const fetchManagerProfileData = async (setManager, setLoading, setError) => {
  setLoading(true);
  try {
    const token = localStorage.getItem("token");
    const response = await axios.get(`${hospitalURL}/manager`, {
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

export const fetchHospitalProfileData = async (setHospital, setLoading, setError) => {
  setLoading(true);
  try {
    const token = localStorage.getItem('token');
    const response = await axios.get(`${hospitalURL}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    setHospital(response.data);
  } catch (error) {
    setError(error?.response?.data?.message || "Error while fetching hospital details. Please try again!");
    console.error('Error in fetching hospital: ', error);
  } finally{
    setLoading(false);
  }
}