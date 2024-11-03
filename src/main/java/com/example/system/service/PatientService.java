package com.example.system.service;

import com.example.system.dto.DoctorDTO;
import com.example.system.dto.ProfileUpdateDTO;
import com.example.system.entity.Doctor;
import com.example.system.entity.MedicalHistory;
import com.example.system.entity.Patient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PatientService {
    void updatePatient(Patient patient, ProfileUpdateDTO updateDTO);
    void addMedicalHistory(Patient patient, List<MedicalHistory> medicalHistories);
    void deletePatient(Patient patient);
    List<DoctorDTO> findDoctorsByDepartment(String department);
}
