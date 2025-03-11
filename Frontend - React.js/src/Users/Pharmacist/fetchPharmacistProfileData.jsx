import axios from "axios";
import { pharmacyURL } from "../../Api & Services/Api.js";

export const fetchPharmacistProfileData = async (setManager, setLoading, setError) => {
  setLoading(true);
  try {
    const token = localStorage.getItem("token");
    const response = await axios.get(`${pharmacyURL}/pharmacist`, {
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

export const fetchPharmacyProfileData = async (setPharmacy, setLoading, setError) => {
  setLoading(true);
  try {
    const token = localStorage.getItem("token");
    const response = await axios.get(`${pharmacyURL}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    setPharmacy(response.data);
  } catch (error) {
    setError("Error fetching profile data!");
    console.error("Error fetching profile data", error);
  } finally{
    setLoading(false);
  }
}