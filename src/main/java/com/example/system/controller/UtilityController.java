package com.example.system.controller;

import com.example.system.entity.Doctor;
import com.example.system.exception.HospitalManagementException;
import com.example.system.repository.DoctorRepo;
import com.example.system.service.DoctorService;
import com.example.system.service.utils.Utility;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
public class UtilityController {

    private final Utility utility;
    private final DoctorRepo doctorRepo;
    private final DoctorService doctorService;

    public UtilityController(Utility utility, DoctorRepo doctorRepo, DoctorService doctorService) {
        this.utility = utility;
        this.doctorRepo = doctorRepo;
        this.doctorService = doctorService;
    }

    @PostMapping("/upload-image")
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            return utility.saveImage(file);
        } catch (IOException e) {
            throw new HospitalManagementException(e.getMessage());
        }
    }

    @DeleteMapping("/delete-image")
    public String deleteImage(@RequestBody Map<String, String> data) {
        String fileName = data.get("fileName");
        try {
            return utility.deleteImage(fileName);
        } catch (IOException e) {
            throw new HospitalManagementException("Failed to delete the image: " + e.getMessage());
        }
    }

    @GetMapping("/check")
    public boolean check(@RequestParam("u") String username) {
        return doctorRepo.findByUsername(username).isPresent();
    }

    @Transactional
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteDoctor(@PathVariable long id) {
        Doctor doctor = doctorRepo.findById(id).orElseThrow(() -> new HospitalManagementException("Doctor not found."));
        doctorService.deleteProfile(doctor);
        return ResponseEntity.ok("Doctor deleted successfully.");
    }

}
