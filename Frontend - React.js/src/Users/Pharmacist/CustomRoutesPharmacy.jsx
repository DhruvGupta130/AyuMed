import { Route, Routes } from 'react-router-dom'
import Sidebar from './Sidebar';
import Pharmacy from './Pharmacy';
import PharmacistProfile from './PharmacistProfile';
import Medications from './Medications';
import "./Pharmacy.css";
import Patients from './Patients';
import Notifications from '../../Pages/Notifications';
import ProfileSettings from '../Manager/ProfileSettings';


const CustomRoutesPharmacy = () => {
  return (
    <div>
      <Sidebar />
      <Routes>
          <Route path='/profile' element={<Pharmacy/>}/>
          <Route path='/pharmacist/profile' element={<PharmacistProfile/>} />
          <Route path='/medications' element = {<Medications/>} />
          <Route path='/patients' element = {<Patients/>} />
          <Route path='/notifications' element={<Notifications/>} />
          <Route path='/settings' element={<ProfileSettings/>} />
      </Routes>
    </div>
  )
}

export default CustomRoutesPharmacy;