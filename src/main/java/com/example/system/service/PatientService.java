package com.example.system.service;

import com.example.system.dto.*;
import com.example.system.entity.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public interface PatientService {
    void updatePatient(Patient patient, String aadhaarId, String mobile, MultipartFile image);
    void updatePassword(Patient patient, Password password);
    Patient getPatientById(long id);
    void addMedicalHistory(Doctor doctor, Patient patient, MedicalHistory historyRequest);
    void addLabResults(MedicalTest medicalTest, long historyId) throws IOException;
    void deletePatient(Patient patient);
    MedicalHistory getMedicalHistoryById(long id);
    PatientDTO getPatientProfile(Patient patient);
    List<PatientDTO> getDoctorsPatient(Doctor doctor);
    List<HospitalPatientDTO> getHospitalPatient(Hospital hospital);
    List<LabTestDTO> getMedicalTests(Patient patient);
    List<LabTestDTO> getMedicalTests(MedicalHistory history);
    void addAddress(Patient patient, Address address);
    void updateAddress(Patient patient, Address address);
    List<TimeSlot> getAvailableSlots(LocalDate date, Doctor doctor);
    List<MedicalHistoryDTO> getMedicalHistory(Patient patient);
}
