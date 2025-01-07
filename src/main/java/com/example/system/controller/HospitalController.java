package com.example.system.controller;

import com.example.system.dto.DoctorDTO;
import com.example.system.dto.HospitalDTO;
import com.example.system.dto.RegistrationDTO;
import com.example.system.dto.UserRole;
import com.example.system.entity.Manager;
import com.example.system.exception.HospitalManagementException;
import com.example.system.repository.UserRepo;
import com.example.system.service.DoctorService;
import com.example.system.service.HospitalService;
import com.example.system.service.utils.Utility;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/hospital")
public class HospitalController {

    private final HospitalService hospitalService;
    private final Utility utility;
    private final DoctorService doctorService;
    private final UserRepo userRepo;

    @Transactional
    @PostMapping("/register")
    public ResponseEntity<String> registerHospital(
            @RequestHeader("Authorization") String token,
            @RequestBody HospitalDTO hospitalDTO
    ) {
        Manager manager = (Manager) utility.getUserFromToken(token);
        hospitalService.registerHospital(hospitalDTO, manager);
        return ResponseEntity.ok("Hospital registered successfully");
    }
    @GetMapping("/manager")
    public ResponseEntity<Manager> getHospitalManager(@RequestHeader("Authorization") String token) {
        Manager manager = (Manager) utility.getUserFromToken(token);
        return ResponseEntity.ok(manager);
    }
    @GetMapping
    public ResponseEntity<HospitalDTO> getHospital(@RequestHeader("Authorization") String token) {
        Manager manager = (Manager) utility.getUserFromToken(token);
        if (manager.getHospital() == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(hospitalService.getHospitalProfile(manager));
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorDTO>> getAllDoctors(@RequestHeader("Authorization") String token) {
        Manager manager = (Manager) utility.getUserFromToken(token);
        return ResponseEntity.ok(hospitalService.getAllDoctors(manager.getHospital()));
    }

    @PostMapping("/doctors/register")
    public ResponseEntity<String> registerDoctor(@RequestHeader("Authorization") String token, @RequestBody RegistrationDTO registrationDTO) {
        userRepo.findByUsername(registrationDTO.getUsername()).ifPresent(_ -> {
            throw new HospitalManagementException("User already exists");
        });
        Manager manager = (Manager) utility.getUserFromToken(token);
        registrationDTO.setHospitalId(manager.getHospital().getId());
        registrationDTO.setRole(UserRole.ROLE_DOCTOR);
        doctorService.registerDoctor(registrationDTO);
        return ResponseEntity.ok("Doctor registered successfully");
    }

    @PostMapping("/doctors/upload")
    public ResponseEntity<String> addDoctors(@RequestHeader("Authorization") String token, @RequestParam("file") MultipartFile file) {
        Manager manager = (Manager) utility.getUserFromToken(token);
        doctorService.saveFromExcel(file, manager.getHospital().getId());
        return ResponseEntity.ok("Doctors added successfully");
    }
}
