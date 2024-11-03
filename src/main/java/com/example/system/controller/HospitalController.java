package com.example.system.controller;

import com.example.system.dto.HospitalDTO;
import com.example.system.entity.Hospital;
import com.example.system.entity.HospitalManager;
import com.example.system.service.HospitalService;
import com.example.system.service.utils.Utility;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/hospital")
public class HospitalController {

    private final HospitalService hospitalService;
    private final Utility utility;

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
}
