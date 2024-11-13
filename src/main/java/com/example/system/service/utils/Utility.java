package com.example.system.service.utils;

import com.example.system.configuration.JwtUtils;
import com.example.system.dto.DoctorDTO;
import com.example.system.dto.PatientDTO;
import com.example.system.entity.Doctor;
import com.example.system.entity.LoginUser;
import com.example.system.entity.Patient;
import com.example.system.exception.HospitalManagementException;
import com.example.system.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class Utility {

    private final JwtUtils jwtUtils;
    private final UserRepo userRepo;
    private final DoctorRepo doctorRepo;
    private final PatientRepo patientRepo;
    private final AdminRepo adminRepo;
    private final ManagerRepo managerRepo;
    private final PharmacistRepo pharmacistRepo;

    public Object getUserFromToken(String token) {
        String userName = jwtUtils.getUserNameFromToken(token);
        LoginUser user = userRepo.findByUsername(userName).orElseThrow(
                () -> new HospitalManagementException("User Not Found"));
        return switch (user.getRole()){
            case ROLE_PATIENT -> patientRepo.findByUsername(userName)
                    .orElseThrow(() -> new HospitalManagementException("Patient Not Found"));
            case ROLE_DOCTOR -> doctorRepo.findByUsername(userName)
                    .orElseThrow(() -> new HospitalManagementException("Doctor Not Found"));
            case ROLE_PHARMACIST -> pharmacistRepo.findByUsername(userName)
                    .orElseThrow(() -> new HospitalManagementException("Pharmacist Not Found"));
            case ROLE_ADMIN -> adminRepo.findByUsername(userName)
                    .orElseThrow(() -> new HospitalManagementException("Admin Not Found"));
            case ROLE_MANAGEMENT -> managerRepo.findByUsername(userName)
                    .orElseThrow(() -> new HospitalManagementException("Manager Not Found"));
        };
    }

    public static PatientDTO getPatientDTO(Patient patient){
        PatientDTO patientDTO = new PatientDTO();
        BeanUtils.copyProperties(patient, patientDTO);
        return patientDTO;
    }

    public static DoctorDTO getDoctorDTO(Doctor doctor){
        DoctorDTO doctorDTO = new DoctorDTO();
        BeanUtils.copyProperties(doctor, doctorDTO);
        return doctorDTO;
    }

}
