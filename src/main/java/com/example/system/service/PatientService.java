package com.example.system.service;

import com.example.system.dto.DoctorDTO;
import com.example.system.dto.Password;
import com.example.system.dto.PatientDTO;
import com.example.system.entity.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Service
public interface PatientService {
    void updatePatient(Patient patient, long aadhaarId, long mobile, String nationality, MultipartFile image);
    void updatePassword(Patient patient, Password password);
    void addMedicalHistory(Patient patient, List<MedicalHistory> medicalHistories);
    void deletePatient(Patient patient);
    PatientDTO getPatientProfile(Patient patient);
    List<DoctorDTO> findDoctorsByDepartment(String department);
    List<MedicalTest> getMedicalTests(Patient patient);
    void addAddress(Patient patient, Address address);
    void updateAddress(Patient patient, Address address);
    List<TimeSlot> getAvailableSlots(LocalDate date, Doctor doctor);
}
