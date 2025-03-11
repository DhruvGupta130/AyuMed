import { useEffect, useState } from 'react';
import PatientDetailsCard from './PatientDetailsCard.jsx';
import { Alert, CircularProgress } from '@mui/material';
import PatientAddressCard from './PatientAddressCard.jsx';
import { fetchPatientProfileData } from './fetchPatientProfileData.jsx';

const PatientProfile = () => {
  const [patient, setPatient] = useState({
    firstName: "",
    lastName: "",
    dateOfBirth: "",
    gender: "",
    email: "",
    mobile: "",
    nationality: "",
    image: "",
    address: {},
    fullName: "",
    aadhaarId: "",
  });

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    fetchPatientProfileData(setPatient, setLoading, setError);
  }, []);

  const refreshProfileData = async () => {
    setLoading(true);
    setError(null);
    await fetchPatientProfileData(setPatient, setLoading, setError);
  };

  if (loading) {
    return <div className='loader'><CircularProgress/></div>;
  }

  if (error) {
    return (
      <div className='error-box'>
        <Alert sx={{ display: "flex", justifyContent: "center", fontSize: "medium" }} severity="error">
          {error}
        </Alert>
      </div>
      );
  }

  return (
    <div className="main-content">
      {patient ? (
        <div className='patient-profile'>
          <PatientDetailsCard patient={patient} refreshProfileData={refreshProfileData} />
          <PatientAddressCard patient={patient} refreshProfileData={refreshProfileData} />
        </div>
      ) : (
        <div>No patient data available</div> 
      )}
    </div>
  );
};

export default PatientProfile;
