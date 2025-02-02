package com.example.system.service.impl;

import com.example.system.dto.*;
import com.example.system.entity.*;
import com.example.system.exception.HospitalManagementException;
import com.example.system.repository.AddressRepo;
import com.example.system.repository.HospitalRepo;
import com.example.system.repository.ManagerRepo;
import com.example.system.service.DoctorService;
import com.example.system.service.HospitalService;
import com.example.system.service.PatientService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class HospitalServiceImpl implements HospitalService {

    private final HospitalRepo hospitalRepo;
    private final ManagerRepo managerRepo;
    private final AddressRepo addressRepo;
    private final PasswordEncoder passwordEncoder;
    private final DoctorService doctorService;
    private final PatientService patientService;

    @Override
    @Transactional
    public void registerHospital(Hospital hospital, Manager manager) {
        try {
            hospital.setManager(manager);
            manager.setHospital(hospital);
            hospitalRepo.save(hospital);
        } catch (Exception e) {
            throw new HospitalManagementException(e.getMessage());
        }
    }

    @Override
    public HospitalDTO getHospitalProfile(Hospital hospital) {
        return new HospitalDTO(
                hospital.getId(), hospital.getHospitalName(),
                hospital.getAddress(), hospital.getEmail(),
                hospital.getMobile(), hospital.getDepartments(),
                hospital.getWebsite(), hospital.getEstablishedYear(),
                hospital.getOverview(), hospital.getSpecialities(),
                hospital.isEmergencyServices(), hospital.getBedCapacity(),
                hospital.getIcuCapacity(), hospital.getOperationTheaters(),
                hospital.getTechnology(), hospital.getAccreditations(),
                hospital.getInsurancePartners(),
                hospital.getFeedbacks(), hospital.getImages()
        );
    }

    @Override
    public void updatePassword(Manager manager, Password password) {
        if(!passwordEncoder.matches(password.getOldPassword(), manager.getLoginUser().getPassword()))
            throw new HospitalManagementException("Provided password is incorrect");
        manager.getLoginUser().setPassword(passwordEncoder.encode(password.getNewPassword()));
        managerRepo.save(manager);
    }

    @Override
    public List<String> getAllDepartments(Hospital hospital) {
        return hospitalRepo.getAllDepartments(hospital);
    }

    @Override
    public List<String> getAllDepartments(){
        return hospitalRepo.getAllDepartments();
    }

    @Override
    public List<HospitalPatientDTO> getAllPatients(Hospital hospital) {
        return patientService.getHospitalPatient(hospital);
    }

    @Override
    public void addPatientLabResult(MedicalTest medicalTest, long historyId) throws IOException {
        patientService.addLabResults(medicalTest, historyId);
    }

    @Override
    public List<LabTestDTO> getPatientLabResults(long medicalId) {
        MedicalHistory medicalHistory = patientService.getMedicalHistoryById(medicalId);
        return patientService.getMedicalTests(medicalHistory);
    }

    @Override
    public List<MedicalHistoryDTO> getPatientsMedicalHistory(long patientId) {
        Patient patient = patientService.getPatientById(patientId);
        if(patient.getMedicalHistories() == null) throw new HospitalManagementException("No medical histories found");
        return patientService.getMedicalHistory(patient);
    }

    @Override
    public ManagerDTO getManagerProfile(Manager manager) {
        return new ManagerDTO(
                manager.getId(), manager.getFirstName(),
                manager.getLastName(), manager.getGender(),
                manager.getEmail(), manager.getMobile(),
                manager.getHospital() ==null ?
                        null : manager.getHospital().getHospitalName(),
                manager.getFullName()
        );
    }

    @Override
    public void updateManagerProfile(Manager manager, ManagerDTO managerDTO) {
        try {
            manager.setFirstName(managerDTO.getFirstName());
            manager.setLastName(managerDTO.getLastName());
            manager.setGender(managerDTO.getGender());
            manager.setEmail(managerDTO.getEmail());
            manager.setMobile(managerDTO.getMobile());
            managerRepo.save(manager);
        } catch (NullPointerException e) {
            throw new HospitalManagementException("Error in updating manager profile: " + e.getMessage());
        }
    }

    @Override
    public List<HospitalDTO> getHospitalsWithinRadius(double latitude, double longitude, double radius) {
        List<Hospital> hospitals = hospitalRepo.findHospitalsWithinRadius(latitude, longitude, radius);
        return hospitals.stream().map(this::getHospitalProfile).toList();
    }

    @Override
    public List<HospitalDTO> searchHospital(String keyword) {
        List<Hospital> hospitals = hospitalRepo.searchByKeyword(keyword);
        return hospitals.stream().map(this::getHospitalProfile).toList();
    }

    @Override
    public List<HospitalDTO> getHospitalsByDepartment(String department) {
       return hospitalRepo.findHospitalByDepartment(department)
               .stream().map(this::getHospitalProfile).toList();
    }

    @Override
    public Hospital getHospitalById(long id) {
        return hospitalRepo.findHospitalById(id)
                .orElseThrow(() -> new HospitalManagementException("Hospital not found"));
    }

    @Override
    public List<DoctorDTO> getAllDoctors(Hospital hospital) {
        return doctorService.getHospitalDoctors(hospital);
    }

    @Override
    public void updateAddress(Manager manager, Address address) {
        Address address1 = addressRepo.findById(manager.getHospital().getAddress().getId()).
                orElseThrow(()-> new HospitalManagementException("Address not found"));
        address.setId(address1.getId());
        addressRepo.save(address);
    }
}
