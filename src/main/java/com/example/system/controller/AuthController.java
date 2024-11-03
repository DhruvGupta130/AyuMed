package com.example.system.controller;

import com.example.system.dto.LoginRequest;
import com.example.system.dto.LoginResponse;
import com.example.system.dto.RegistrationDTO;
import com.example.system.configuration.JwtUtils;
import com.example.system.entity.Admin;
import com.example.system.entity.Doctor;
import com.example.system.entity.LoginUser;
import com.example.system.entity.Patient;
import com.example.system.exception.HospitalManagementException;
import com.example.system.repository.AdminRepo;
import com.example.system.repository.DoctorRepo;
import com.example.system.repository.PatientRepo;
import com.example.system.repository.UserRepo;
import com.example.system.service.AuthService;
import com.example.system.service.Impl.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class AuthController {

    private final UserRepo userRepo;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistrationDTO registrationDTO) {
        if(userRepo.findByUsername(registrationDTO.getUsername()).isPresent()){
            throw new HospitalManagementException("Username is already registered. Please login");
        }
        switch (registrationDTO.getRole()) {
            case ROLE_PATIENT -> authService.createPatient(registrationDTO);
            case ROLE_DOCTOR -> authService.createDoctor(registrationDTO);
            case ROLE_ADMIN -> authService.createAdmin(registrationDTO);
            case ROLE_MANAGEMENT -> authService.createManager(registrationDTO);
            default -> ResponseEntity.badRequest().body("Invalid role specified.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse response = authService.loginService(loginRequest);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AuthenticationException e) {
            throw new HospitalManagementException("Invalid username or password", e);
        }
    }
}
