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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class Utility {

    private final JwtUtils jwtUtils;
    private final UserRepo userRepo;
    private final DoctorRepo doctorRepo;
    private final PatientRepo patientRepo;
    private final AdminRepo adminRepo;
    private final ManagerRepo managerRepo;
    private final PharmacistRepo pharmacistRepo;

    @Value("${file.storage.path}")
    private static String storagePath;

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

    public static String saveImage(MultipartFile imageFile) throws IOException {
        String patientDirPath = storagePath + "/";
        String filePath = patientDirPath + "/" + imageFile.getOriginalFilename();
        Path parentDir = Paths.get(patientDirPath);
        Path dest = Paths.get(filePath);
        if(Files.notExists(parentDir)) {
            Files.createDirectories(parentDir);
        }
        imageFile.transferTo(dest);
        return filePath;
    }

}
