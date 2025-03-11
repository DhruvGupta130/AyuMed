import { Route, Routes } from 'react-router-dom';
import HospitalProfilePage from './HospitalProfilePage';
import PharmacyProfilePage from './PharmacyProfilePage';
import DoctorProfile from './DoctorProfile';
import MedicationProfile from './MedicationProfile';
import AppointmentBookingPage from './AppointmentBookingPage';
import MedicineStore from "./MedicineStore.jsx";
import ConsultDoctorPage from "./ConsultDoctorPage.jsx";

const CustomRoutesPages = () => {
    return (
        <Routes>
            <Route path="/profile/hospital/:id" element={<HospitalProfilePage />} />
            <Route path="/profile/pharmacy/:id" element={<PharmacyProfilePage />} />
            <Route path="/profile/doctor/:id" element={<DoctorProfile />} />
            <Route path="/profile/medication/:id" element={<MedicationProfile />} />
            <Route path="/appointment/book" element={<AppointmentBookingPage/>} />
            <Route path="/order-medicines" element={<MedicineStore />} />
            <Route path="/consult-doctor" element={<ConsultDoctorPage/>} />
        </Routes>
    );
};

export default CustomRoutesPages;