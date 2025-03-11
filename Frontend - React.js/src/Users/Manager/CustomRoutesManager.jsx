import { Route, Routes } from 'react-router-dom'
import Sidebar from './Sidebar';
import Doctors from './Doctors.jsx';
import Hospital from './Hospital.jsx';
import ManagerProfile from './ManagerProfile.jsx';
import Patients from './Patients.jsx';
import FeedbackPage from './FeedbackPage.jsx';
import ProfileSettings from './ProfileSettings.jsx';
import "./Hospital.css";


const CustomRoutesManager = () => {
  return (
    <div>
      <Sidebar />
      <Routes>
          <Route path='/profile' element={<Hospital />}/>
          <Route path='/manager/profile' element={<ManagerProfile/>}/>
          <Route path='/doctors' element={<Doctors />} />
          <Route path='/patients' element={<Patients />} />
          <Route path='/feedbacks' element={<FeedbackPage/>} />
          <Route path='/settings' element={<ProfileSettings/>} />
      </Routes>
    </div>
  )
}

export default CustomRoutesManager;