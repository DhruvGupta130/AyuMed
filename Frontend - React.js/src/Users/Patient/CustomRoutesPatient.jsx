import { Route, Routes } from 'react-router-dom'
import Sidebar from './Sidebar.jsx';
import PatientDocument from './PatientDocument.jsx';
import MedicalRecords from './MedicalRecords.jsx';
import Appointments from './Appointments.jsx';
import LabTest from './LabTest.jsx';
import PatientProfile from './PatientProfile.jsx';
import ProfileSettings from './ProfileSettings.jsx';
import Medications from './Medications.jsx';
import Notifications from '../../Pages/Notifications.jsx';
import './Patient.css';

const CustomRoutesPatient = () => {
  return (
    <div>
      <Sidebar />
      <Routes>
          <Route path='/profile' element={<PatientProfile />} />
          <Route path='/appointments' element={<Appointments />} />
          <Route path='/documents' element={<PatientDocument/>} />
          <Route path='/records' element={<MedicalRecords/>} />
          <Route path='/medications' element={<Medications/>} />
          <Route path='/lab-results' element={<LabTest/>} />
          <Route path='/notifications' element={<Notifications/>} />
          <Route path='/settings' element={<ProfileSettings/>} />
      </Routes>
    </div>
  )
}

export default CustomRoutesPatient;