package com.example.system.controller;

import com.example.system.dto.HospitalDTO;
import com.example.system.entity.Manager;
import com.example.system.service.DoctorService;
import com.example.system.service.HospitalService;
import com.example.system.service.utils.Utility;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping("/api/hospital")
public class HospitalController {

    private final HospitalService hospitalService;
    private final Utility utility;
    private final DoctorService doctorService;

    @Transactional
    @PostMapping("/register")
    public ResponseEntity<String> registerHospital(@RequestHeader("Authorization") String token, @RequestBody HospitalDTO hospitalDTO) {
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
        return ResponseEntity.ok(hospitalService.getHospitalProfile(manager));
    }

    @PostMapping("/upload")
    public ResponseEntity<String> addDoctors(@RequestHeader("Authorization") String token, @RequestParam("file") MultipartFile file) {
        Manager manager = (Manager) utility.getUserFromToken(token);
        doctorService.saveFromExcel(file, manager.getHospital().getId());
        return ResponseEntity.ok("Doctors added successfully");
    }
}
