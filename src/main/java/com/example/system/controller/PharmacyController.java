package com.example.system.controller;

import com.example.system.dto.PharmacyDTO;
import com.example.system.entity.Medication;
import com.example.system.entity.Pharmacist;
import com.example.system.exception.HospitalManagementException;
import com.example.system.service.PharmacyService;
import com.example.system.service.utils.Utility;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/pharmacy")
@AllArgsConstructor
public class PharmacyController {

    private final PharmacyService pharmacyService;
    private final Utility utility;

    @Transactional
    @PostMapping("/register")
    public ResponseEntity<String> registerPharmacy(@RequestHeader("Authorization") String token, @RequestBody PharmacyDTO pharmacy) {
        Pharmacist pharmacist = (Pharmacist) utility.getUserFromToken(token);
        if(pharmacist.getPharmacy()!=null) throw new HospitalManagementException("Your pharmacy is already registered");
        pharmacyService.createPharmacy(pharmacy, pharmacist);
        return ResponseEntity.ok().body("Pharmacy successfully registered!");
    }

    @Transactional
    @PutMapping("/update")
    public ResponseEntity<String> updatePharmacy(@RequestHeader("Authorization") String token, @RequestBody PharmacyDTO pharmacy) {
        Pharmacist pharmacist = (Pharmacist) utility.getUserFromToken(token);
        if(pharmacist.getPharmacy()==null) throw new HospitalManagementException("Your pharmacy is not registered with us");
        pharmacyService.updatePharmacy(pharmacy, pharmacist);
        return ResponseEntity.ok().body("Pharmacy successfully updated!");
    }

    @PostMapping("/medications/add")
    public ResponseEntity<String> addMedication(@RequestHeader("Authorization") String token, @RequestBody Medication medication){
        Pharmacist pharmacist = (Pharmacist) utility.getUserFromToken(token);
        pharmacyService.updatePharmacy(medication, pharmacist);
        return ResponseEntity.ok().body("Medication successfully added!");
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadPharmacy(@RequestHeader("Authorization") String token, @RequestParam("file") MultipartFile file) {
        Pharmacist pharmacist = (Pharmacist) utility.getUserFromToken(token);
        pharmacyService.saveFromExcel(file, pharmacist);
        return ResponseEntity.ok().body("Medications uploaded successfully!");
    }

    @PutMapping("/setStatus")
    public ResponseEntity<String> toggleStatus(@RequestHeader("Authorization") String token) {
        Pharmacist pharmacist = (Pharmacist) utility.getUserFromToken(token);
        pharmacyService.setStatus(pharmacist.getPharmacy());
        return ResponseEntity.ok().body("Status successfully updated to "+ pharmacist.getPharmacy().isOpen());
    }

    @GetMapping("/medications")
    public ResponseEntity<List<Medication>> getMedications() {
        return ResponseEntity.ok().body(pharmacyService.getMedications());
    }

    @GetMapping
    public ResponseEntity<PharmacyDTO> getPharmacy(@RequestHeader("Authorization") String token) {
        Pharmacist pharmacist = (Pharmacist) utility.getUserFromToken(token);
        return ResponseEntity.ok().body(pharmacyService.getPharmacy(pharmacist));
    }

    @GetMapping("/pharmacist")
    public ResponseEntity<Pharmacist> getPharmacyDetail(@RequestHeader("Authorization") String token) {
        Pharmacist pharmacist = (Pharmacist) utility.getUserFromToken(token);
        return ResponseEntity.ok().body(pharmacist);
    }
}
