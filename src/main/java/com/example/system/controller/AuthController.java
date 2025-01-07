package com.example.system.controller;

import com.example.system.dto.*;
import com.example.system.exception.HospitalManagementException;
import com.example.system.repository.UserRepo;
import com.example.system.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class AuthController {

    private final UserRepo userRepo;
    private final AuthService authService;

    @Transactional
    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody RegistrationDTO registrationDTO) {
        Response response = new Response();
        try {
            if (userRepo.findByUsername(registrationDTO.getUsername()).isPresent()) {
                throw new HospitalManagementException("Username is already registered. Please login");
            }
            switch (registrationDTO.getRole()) {
                case ROLE_PATIENT -> authService.createPatient(registrationDTO);
                case ROLE_DOCTOR -> authService.createDoctor(registrationDTO);
                case ROLE_ADMIN -> authService.createAdmin(registrationDTO);
                case ROLE_MANAGEMENT -> authService.createManager(registrationDTO);
                case ROLE_PHARMACIST -> authService.createPharmacist(registrationDTO);
                case null, default ->
                        throw new HospitalManagementException("Invalid role specified");
            }
            response.setMessage("User registered successfully");
            response.setStatus(HttpStatus.CREATED);
        } catch (HospitalManagementException e) {
            response.setError(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.setError("An unexpected error occurred during registration");
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse responseDTO = authService.loginService(loginRequest);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (AuthenticationException e) {
            throw new HospitalManagementException("Invalid username or password", e);
        }
    }
}
