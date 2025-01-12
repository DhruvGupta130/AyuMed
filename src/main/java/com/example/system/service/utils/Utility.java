package com.example.system.service.utils;

import com.example.system.configuration.JwtUtils;
import com.example.system.entity.LoginUser;
import com.example.system.exception.HospitalManagementException;
import com.example.system.repository.*;
import org.springframework.stereotype.Service;

@Service
public class Utility {

    private final JwtUtils jwtUtils;
    private final UserRepo userRepo;
    private final DoctorRepo doctorRepo;
    private final PatientRepo patientRepo;
    private final AdminRepo adminRepo;
    private final ManagerRepo managerRepo;
    private final PharmacistRepo pharmacistRepo;



    public Utility(JwtUtils jwtUtils, UserRepo userRepo, DoctorRepo doctorRepo, PatientRepo patientRepo, AdminRepo adminRepo, ManagerRepo managerRepo, PharmacistRepo pharmacistRepo) {
        this.jwtUtils = jwtUtils;
        this.userRepo = userRepo;
        this.doctorRepo = doctorRepo;
        this.patientRepo = patientRepo;
        this.adminRepo = adminRepo;
        this.managerRepo = managerRepo;
        this.pharmacistRepo = pharmacistRepo;
    }

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
}
