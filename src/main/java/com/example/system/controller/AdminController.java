package com.example.system.controller;

import com.example.system.dto.AppointmentDTO;
import com.example.system.dto.DoctorDTO;
import com.example.system.dto.PatientDTO;
import com.example.system.entity.*;
import com.example.system.exception.HospitalManagementException;
import com.example.system.service.AdminService;
import com.example.system.service.utils.Utility;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final Utility utility;
    private final AdminService adminService;

    @GetMapping("/profile")
    public ResponseEntity<Admin> getProfile(@RequestHeader("Authorization") String token) {
        Admin admin = (Admin) utility.getUserFromToken(token);
        if(admin == null) throw new HospitalManagementException("Admin not found");
        return ResponseEntity.ok(admin);
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorDTO>> getAllDoctors() {
        return ResponseEntity.ok(adminService.getAllDoctors());
    }

    @GetMapping("/patients")
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        return ResponseEntity.ok(adminService.getAllPatients());
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentDTO>> getAllAppointments() {
        return ResponseEntity.ok(adminService.getAllAppointments());
    }

    @Transactional
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestHeader("Authorization") String token) {
        Admin admin = (Admin) utility.getUserFromToken(token);
        if(admin == null) throw new HospitalManagementException("Admin not found");
        adminService.deleteProfile(admin);
        return ResponseEntity.ok("Admin deleted successfully");
    }

}
