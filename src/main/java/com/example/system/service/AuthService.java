package com.example.system.service;

import com.example.system.dto.LoginRequest;
import com.example.system.dto.LoginResponse;
import com.example.system.dto.RegistrationDTO;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    void createPatient(RegistrationDTO registrationDTO);
    void createDoctor(RegistrationDTO registrationDTO);
    void createAdmin(RegistrationDTO registrationDTO);
    void createManager(RegistrationDTO registrationDTO);
    LoginResponse loginService(LoginRequest request);
}
