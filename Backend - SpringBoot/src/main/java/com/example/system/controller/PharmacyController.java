package com.example.system.controller;

import com.example.system.dto.*;
import com.example.system.entity.Medication;
import com.example.system.entity.Pharmacist;
import com.example.system.entity.Pharmacy;
import com.example.system.exception.HospitalManagementException;
import com.example.system.service.PharmacyService;
import com.example.system.service.utils.Utility;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Response> registerPharmacy(@RequestHeader("Authorization") String token,
                                                     @RequestBody Pharmacy pharmacy) {
        Response response = new Response();
        try {
            Pharmacist pharmacist = (Pharmacist) utility.getUserFromToken(token);
            if(pharmacist == null) throw new HospitalManagementException("Pharmacist not found.");
            if (pharmacist.getPharmacy() != null)
                throw new HospitalManagementException("Your pharmacy is already added.");
            pharmacyService.createPharmacy(pharmacy, pharmacist);
            response.setMessage("Pharmacy successfully registered.");
            response.setStatus(HttpStatus.OK);
        } catch (HospitalManagementException e) {
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Transactional
    @PutMapping("/update")
    public ResponseEntity<Response> updatePharmacy(@RequestHeader("Authorization") String token,
                                                   @RequestBody PharmacyDTO pharmacy) {
        Response response = new Response();
        try {
            Pharmacist pharmacist = (Pharmacist) utility.getUserFromToken(token);
            if(pharmacist == null) throw new HospitalManagementException("Pharmacist not found.");
            if (pharmacist.getPharmacy() == null)
                throw new HospitalManagementException("Your pharmacy is not registered with us");
            pharmacyService.updatePharmacy(pharmacy, pharmacist);
            response.setMessage("Pharmacy successfully updated.");
            response.setStatus(HttpStatus.OK);
        } catch (HospitalManagementException e) {
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/medications/add")
    public ResponseEntity<Response> addMedication(@RequestHeader("Authorization") String token,
                                                  @RequestBody Medication medication){
        Response response = new Response();
        try {
            Pharmacist pharmacist = (Pharmacist) utility.getUserFromToken(token);
            if(pharmacist == null) throw new HospitalManagementException("Pharmacist not found.");
            pharmacyService.updatePharmacy(medication, pharmacist);
            response.setMessage("Medication successfully added.");
            response.setStatus(HttpStatus.OK);
        } catch (HospitalManagementException e) {
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/medications/upload")
    public ResponseEntity<Response> uploadPharmacy(@RequestHeader("Authorization") String token,
                                                   @RequestParam("file") MultipartFile file) {
        Response response = new Response();
        try {
            Pharmacist pharmacist = (Pharmacist) utility.getUserFromToken(token);
            if(pharmacist == null) throw new HospitalManagementException("Pharmacist not found.");
            pharmacyService.saveFromExcel(file, pharmacist);
            response.setMessage("Pharmacy successfully uploaded.");
            response.setStatus(HttpStatus.OK);
        } catch (HospitalManagementException e) {
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/setStatus")
    public ResponseEntity<Response> toggleStatus(@RequestHeader("Authorization") String token) {
        Response response = new Response();
        try {
            Pharmacist pharmacist = (Pharmacist) utility.getUserFromToken(token);
            if(pharmacist == null) throw new HospitalManagementException("Pharmacist not found.");
            pharmacyService.setStatus(pharmacist.getPharmacy());
            response.setMessage("Pharmacy status successfully updated to " + (pharmacist.getPharmacy().isOpen() ? "opened" : "closed"));
            response.setStatus(HttpStatus.OK);
        } catch (HospitalManagementException e) {
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/medications")
    public ResponseEntity<List<MedicationDTO>> getMedications(@RequestHeader("Authorization") String token) {
        Pharmacist pharmacist = (Pharmacist) utility.getUserFromToken(token);
        if(pharmacist == null) throw new HospitalManagementException("Pharmacist not found.");
        return ResponseEntity.ok().body(pharmacyService.getMedicationsByPharmacy(pharmacist.getPharmacy()));
    }

    @GetMapping("/patients")
    public ResponseEntity<List<PharmacyPatientsDTO>> getPatients(@RequestHeader("Authorization") String token) {
        Pharmacist pharmacist = (Pharmacist) utility.getUserFromToken(token);
        if (pharmacist.getPharmacy() == null) throw new HospitalManagementException("Please register pharmacy first!");
        List<PharmacyPatientsDTO> allPatients = pharmacyService.getPharmacyPatients(pharmacist.getPharmacy());
        return ResponseEntity.ok(allPatients);
    }

    @GetMapping
    public ResponseEntity<?> getPharmacy(@RequestHeader("Authorization") String token) {
        Response response = new Response();
        response.setStatus(HttpStatus.OK);
        try {
            Pharmacist pharmacist = (Pharmacist) utility.getUserFromToken(token);
            if (pharmacist.getPharmacy() == null) {
                response.setMessage("Your Pharmacy is not registered.");
                response.setStatus(HttpStatus.NO_CONTENT);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(pharmacyService.getPharmacyProfile(pharmacist.getPharmacy()));
            }
        } catch (HospitalManagementException e) {
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.setMessage("Error retrieving hospital: " + e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/pharmacist")
    public ResponseEntity<PharmacistDTO> getPharmacyDetail(@RequestHeader("Authorization") String token) {
        Pharmacist pharmacist = (Pharmacist) utility.getUserFromToken(token);
        if(pharmacist == null) throw new HospitalManagementException("Pharmacist not found.");
        return ResponseEntity.ok().body(pharmacyService.getPharmacistProfile(pharmacist));
    }
}
