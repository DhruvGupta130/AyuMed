package com.example.system.service;

import com.example.system.dto.*;
import com.example.system.entity.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface HospitalService {

    void registerHospital(Hospital hospital, Manager manager);
    HospitalDTO getHospitalProfile(Hospital hospital);

    void updatePassword(Manager manager, Password password);
    void updatePassword(Manager manager, String password);
    List<String> getAllDepartments();
    List<HospitalPatientDTO> getAllPatients(Hospital hospital);
    void addPatientLabResult(MedicalTest medicalTest, long historyId) throws IOException;
    List<LabTestDTO> getPatientLabResults(long medicalId);
    List<MedicalHistoryDTO> getPatientsMedicalHistory(long patientId);
    ManagerDTO getManagerProfile(Manager manager);
    void updateManagerProfile(Manager manager, ManagerDTO managerDTO);
    List<HospitalDTO> getHospitalsWithinRadius(double latitude, double longitude, double radius);
    List<HospitalDTO> searchHospital(String keyword);
    List<HospitalDTO> getHospitalsByDepartment(String department);
    Hospital getHospitalById(long id);
    HospitalDTO getHospitalDtoById(long id);
    List<DoctorDTO> getAllDoctors(Hospital hospital);
    void updateAddress(Manager manager, Address address);
}
