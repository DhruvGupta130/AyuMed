package com.example.system.controller;

import com.example.system.dto.Response;
import com.example.system.entity.Doctor;
import com.example.system.entity.Manager;
import com.example.system.entity.Patient;
import com.example.system.entity.Pharmacist;
import com.example.system.exception.HospitalManagementException;
import com.example.system.service.DoctorService;
import com.example.system.service.HospitalService;
import com.example.system.service.PatientService;
import com.example.system.service.PharmacyService;
import com.example.system.service.utils.OtpService;
import com.example.system.service.utils.Utility;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class ForgotPassController {

    private final OtpService otpService;
    private final Utility utility;
    private final PatientService patientService;
    private final HospitalService hospitalService;
    private final PharmacyService pharmacyService;
    private final DoctorService doctorService;

    @PostMapping("/forgot-password")
    public ResponseEntity<Response> resetPassword(@RequestParam String email) {
        Response response = new Response();
        try {
            utility.verifyEmail(email);
            otpService.generateAndSendOtp(email, "OTP to reset password");
            response.setMessage("OTP sent successfully.");
            response.setStatus(HttpStatus.OK);
        } catch (HospitalManagementException e) {
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.setMessage("An unexpected error occurred: " + e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/update/password")
    public ResponseEntity<Response> updatePassword(@RequestParam String password, @RequestParam String email, @RequestParam String otp) {
        Response response = new Response();
        try {
            if(otp == null || otp.isEmpty()) {
                throw new HospitalManagementException("Error while updating Password. Please try again.");
            }
            if (otpService.validateOtp(email, otp)) {
                throw new HospitalManagementException("Invalid or expired OTP.");
            }
            Object user = utility.getUserFromEmail(email);
            switch (user) {
                case Patient patient -> patientService.updatePassword(patient, password);
                case Manager manager -> hospitalService.updatePassword(manager, password);
                case Pharmacist pharmacist -> pharmacyService.updatePassword(pharmacist, password);
                case Doctor doctor -> doctorService.updatePassword(doctor, password);
                case null, default ->
                        throw new HospitalManagementException("User type not supported!");
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