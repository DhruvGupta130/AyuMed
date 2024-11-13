package com.example.system.service;

import com.example.system.dto.DoctorDTO;
import com.example.system.dto.ProfileUpdateDTO;
import com.example.system.entity.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface PatientService {
    void updatePatient(Patient patient, ProfileUpdateDTO updateDTO);
    void addMedicalHistory(Patient patient, List<MedicalHistory> medicalHistories);
    void deletePatient(Patient patient);
    List<DoctorDTO> findDoctorsByDepartment(String department);
    void updateAddress(Patient patient, Address address);
    List<TimeSlot> getAvailableSlots(LocalDate date, Doctor doctor);
}
