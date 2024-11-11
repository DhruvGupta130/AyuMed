package com.example.system.controller;

import com.example.system.dto.HospitalDTO;
import com.example.system.entity.Hospital;
import com.example.system.entity.HospitalManager;
import com.example.system.exception.HospitalManagementException;
import com.example.system.service.DoctorService;
import com.example.system.service.HospitalService;
import com.example.system.service.utils.Utility;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping("/api/hospital")
public class HospitalController {

    private final HospitalService hospitalService;
    private final Utility utility;
    private final DoctorService doctorService;

    @PostMapping("/register")
    public ResponseEntity<String> registerHospital(@RequestHeader("Authorization") String token, @RequestBody HospitalDTO hospitalDTO) {
        HospitalManager manager = (HospitalManager) utility.getUserFromToken(token);
        hospitalService.registerHospital(hospitalDTO, manager);
        return ResponseEntity.ok("Hospital registered successfully");
    }
    @GetMapping("/manager")
    public ResponseEntity<HospitalManager> getHospitalManager(@RequestHeader("Authorization") String token) {
        HospitalManager manager = (HospitalManager) utility.getUserFromToken(token);
        return ResponseEntity.ok(hospitalService.getHospitalManagerProfile(manager));
    }
    @GetMapping
    public ResponseEntity<Hospital> getHospital(@RequestHeader("Authorization") String token) {
        HospitalManager manager = (HospitalManager) utility.getUserFromToken(token);
        return ResponseEntity.ok(hospitalService.getHospitalProfile(manager));
    }

    @PostMapping("/upload")
    public ResponseEntity<String> addDoctors(@RequestHeader("Authorization") String token, @RequestParam("file") MultipartFile file) {
        HospitalManager manager = (HospitalManager) utility.getUserFromToken(token);
        try{
            doctorService.saveFromExcel(file, manager.getHospital().getId());
        }catch (Exception e){
            throw new  HospitalManagementException(e.getMessage());
        }
        return ResponseEntity.ok("Doctors added successfully");
    }
}
