package com.example.system.controller;

import com.example.system.dto.Password;
import com.example.system.dto.ProfileUpdateDTO;
import com.example.system.dto.Response;
import com.example.system.entity.*;
import com.example.system.entity.Pharmacist;
import com.example.system.entity.Doctor;
import com.example.system.entity.Manager;
import com.example.system.entity.Patient;
import com.example.system.exception.HospitalManagementException;
import com.example.system.service.*;
import com.example.system.service.utils.Utility;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class UpdateController {

    private final Utility utility;
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final HospitalService hospitalService;
    private final PharmacyService pharmacyService;

    @Transactional
    @PutMapping("/doctor/updateProfile")
    public ResponseEntity<Response> updateProfile(@RequestHeader("Authorization") String token,
                                                  @ModelAttribute ProfileUpdateDTO profileUpdateDTO,
                                                  @RequestParam("image") MultipartFile image) {
        Response response = new Response();
        Object user = utility.getUserFromToken(token);
        if (!(user instanceof Doctor doctor))
            throw new HospitalManagementException("Unauthorized access: User is not a doctor.");
        try {
            if (doctor.getSchedules().isEmpty()) {
                doctorService.updateDoctor(doctor, profileUpdateDTO, image);
                response.setMessage("Profile updated successfully");
                response.setStatus(HttpStatus.OK);
            } else {
                response.setMessage("Profile is already up-to-date");
                response.setStatus(HttpStatus.BAD_REQUEST);
            }
        } catch (HospitalManagementException e) {
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.setMessage("Error updating profile: " + e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/address")
    public ResponseEntity<Response> updateAddress(@RequestHeader("Authorization") String token,
                                                  @RequestBody Address address){
        Response response = new Response();
        try {
            Object user = utility.getUserFromToken(token);
            switch (user) {
                case Patient patient -> patientService.updateAddress(patient, address);
                case Manager manager -> hospitalService.updateAddress(manager, address);
                case Pharmacist pharmacist -> pharmacyService.updateAddress(pharmacist, address);
                case null, default ->
                        throw new HospitalManagementException("User type not supported for address update");
            }
            response.setMessage("Address updated successfully");
            response.setStatus(HttpStatus.OK);
        } catch (HospitalManagementException e) {
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.setMessage("Error while updating address: " + e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/update/password")
    public ResponseEntity<Response> updatePassword(@RequestHeader("Authorization") String token, @RequestBody Password password) {
        Response response = new Response();
        try {
            Object user = utility.getUserFromToken(token);
            switch (user) {
                case Patient patient -> patientService.updatePassword(patient, password);
                case Manager manager -> hospitalService.updatePassword(manager, password);
                case Pharmacist pharmacist -> pharmacyService.updatePassword(pharmacist, password);
                case Doctor doctor -> doctorService.updatePassword(doctor, password);
                case null, default ->
                        throw new HospitalManagementException("User type not supported for password update");
            }
            response.setMessage("Password updated successfully");
            response.setStatus(HttpStatus.OK);
        } catch (HospitalManagementException e) {
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.setMessage("Error while updating password: " + e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
