package com.example.system.service.utils;

import com.example.system.configuration.JwtUtils;
import com.example.system.entity.LoginUser;
import com.example.system.exception.HospitalManagementException;
import com.example.system.repository.*;
import lombok.AllArgsConstructor;
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

    public void verifyEmail(String email) throws HospitalManagementException {
        userRepo.findByEmail(email).orElseThrow(() -> new HospitalManagementException("No user found with provided email address!"));
    }

    public Object getUserFromEmail(String email) {
        LoginUser user = userRepo.findByEmail(email).orElseThrow(
                () -> new HospitalManagementException("User Not Found"));
        return switch (user.getRole()){
            case ROLE_PATIENT -> patientRepo.findByEmail(email)
                    .orElseThrow(() -> new HospitalManagementException("Patient Not Found"));
            case ROLE_DOCTOR -> doctorRepo.findByEmail(email)
                    .orElseThrow(() -> new HospitalManagementException("Doctor Not Found"));
            case ROLE_PHARMACIST -> pharmacistRepo.findByEmail(email)
                    .orElseThrow(() -> new HospitalManagementException("Pharmacist Not Found"));
            case ROLE_ADMIN -> adminRepo.findByEmail(email)
                    .orElseThrow(() -> new HospitalManagementException("Admin Not Found"));
            case ROLE_MANAGEMENT -> managerRepo.findByEmail(email)
                    .orElseThrow(() -> new HospitalManagementException("Manager Not Found"));
        };
    }
}
