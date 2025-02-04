package com.example.system.controller;

import com.example.system.dto.*;
import com.example.system.exception.HospitalManagementException;
import com.example.system.repository.UserRepo;
import com.example.system.service.AuthService;
import com.example.system.service.utils.OtpService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class AuthController {

    private final UserRepo userRepo;
    private final AuthService authService;
    private final OtpService otpService;

    @PostMapping("/otp/send")
    public ResponseEntity<Response> register(@RequestBody RegistrationDTO registrationDTO) {
        Response response = new Response();
        try {
            if (userRepo.findByUsername(registrationDTO.getUsername()).isPresent()) {
                throw new HospitalManagementException("Username is already registered. Please login");
            }
            if(userRepo.findByEmail(registrationDTO.getEmail()).isPresent()) {
                throw new HospitalManagementException("Email is already registered. Please login");
            }
            otpService.generateAndSendOtp(registrationDTO.getEmail(), "OTP to register in AyuMed");
            response.setMessage("OTP sent successfully. Please verify OTP to complete registration.");
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

    @GetMapping("/get-username")
    public ResponseEntity<Response> getUsername(@RequestParam String email) {
        Response response = new Response();

        return userRepo.findByEmail(email)
                .map(user -> {
                    response.setMessage("Username found: " + user.getUsername());
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    response.setMessage("User not found for email: " + email);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    @PostMapping("/register")
    public ResponseEntity<Response> completeRegistration(@RequestBody RegistrationDTO registrationDTO) {
        Response response = new Response();
        try {
            if(registrationDTO.getOtp() == null || registrationDTO.getOtp().isEmpty()) {
                throw new HospitalManagementException("Error while registering user. Please try again.");
            }
            if (otpService.validateOtp(registrationDTO.getEmail(), registrationDTO.getOtp())) {
                throw new HospitalManagementException("Invalid or expired OTP.");
            }
            if (userRepo.findByUsername(registrationDTO.getUsername()).isPresent()) {
                throw new HospitalManagementException("Username is already registered. Please login");
            }

            switch (registrationDTO.getRole()) {
                case ROLE_PATIENT -> authService.createPatient(registrationDTO);
                case ROLE_ADMIN -> authService.createAdmin(registrationDTO);
                case ROLE_MANAGEMENT -> authService.createManager(registrationDTO);
                case ROLE_PHARMACIST -> authService.createPharmacist(registrationDTO);
                case null, default -> throw new HospitalManagementException("Invalid role specified");
            }

            response.setMessage("User registered successfully");
            response.setStatus(HttpStatus.CREATED);

        } catch (HospitalManagementException e) {
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.setMessage("An unexpected error occurred: " + e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }



    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        if(userRepo.findByUsername(loginRequest.getUsername()).isEmpty()) {
            throw new HospitalManagementException("User is not registered.");
        }
        LoginResponse responseDTO = authService.loginService(loginRequest);
        return new ResponseEntity<>(responseDTO, responseDTO.getStatus());
    }
}
