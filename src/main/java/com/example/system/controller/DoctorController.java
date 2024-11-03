package com.example.system.controller;

import com.example.system.entity.*;
import com.example.system.exception.HospitalManagementException;
import com.example.system.service.DoctorService;
import com.example.system.service.utils.Utility;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctor")
@AllArgsConstructor
public class DoctorController {

    private final Utility utility;
    private final DoctorService doctorService;

    @GetMapping("/profile")
    public ResponseEntity<Doctor> getDoctorProfile(@RequestHeader("Authorization") String token) {
        Doctor doctor = (Doctor) utility.getUserFromToken(token);
        if (doctor == null) throw new HospitalManagementException("Doctor not found");
        return ResponseEntity.ok(doctor);
    }

    @Transactional
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteDoctor(@RequestHeader("Authorization") String token) {
        Doctor doctor = (Doctor) utility.getUserFromToken(token);
        if (doctor == null) throw new HospitalManagementException("Doctor not found.");
        doctorService.deleteProfile(doctor);
        return ResponseEntity.ok("Doctor deleted successfully.");
    }


}
