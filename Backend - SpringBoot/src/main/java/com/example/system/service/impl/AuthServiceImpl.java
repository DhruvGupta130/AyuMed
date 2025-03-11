package com.example.system.service.impl;

import com.example.system.configuration.JwtUtils;
import com.example.system.dto.EmailStructures;
import com.example.system.dto.LoginRequest;
import com.example.system.dto.LoginResponse;
import com.example.system.dto.RegistrationDTO;
import com.example.system.entity.*;
import com.example.system.entity.Pharmacist;
import com.example.system.entity.Doctor;
import com.example.system.entity.Manager;
import com.example.system.entity.Patient;
import com.example.system.exception.HospitalManagementException;
import com.example.system.repository.*;
import com.example.system.service.AuthService;
import com.example.system.service.utils.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtils jwtUtils;
    private final PatientRepo patientRepo;
    private final DoctorRepo doctorRepo;
    private final AdminRepo adminRepo;
    private final ManagerRepo managerRepo;
    private final HospitalRepo hospitalRepo;
    private final PharmacistRepo pharmacistRepo;
    private final EmailStructures emailStructures;
    private final EmailService emailService;

    @Override
    @Transactional
    public void createPatient(RegistrationDTO registrationDTO) {
        try {
            Patient patient = new Patient();
            setCommonAttributes(patient.getLoginUser(), registrationDTO);
            patient.setFirstName(registrationDTO.getFirstName());
            patient.setLastName(registrationDTO.getLastName());
            patient.setDateOfBirth(registrationDTO.getDateOfBirth());
            patient.setGender(registrationDTO.getGender());
            patient.setMobile(registrationDTO.getMobile());
            patientRepo.save(patient);
            String registration = emailStructures.generateRegistrationEmail(patient.getFullName());
            emailService.sendEmail(
                    patient.getLoginUser().getEmail(),
                    "🎇 Welcome to AyuMed! Your Account is Now Active",
                    registration
            );
        } catch (Exception e) {
            throw new HospitalManagementException("Failed to create patient", e);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('MANAGEMENT')")
    public void createDoctor(RegistrationDTO registrationDTO) {
        try {
            Hospital hospital = hospitalRepo.findById(registrationDTO.getHospitalId()).orElseThrow(
                    () -> new HospitalManagementException("No Hospital found"));
            if (hospital.getDoctors() == null) hospital.setDoctors(new ArrayList<>());
            if (hospital.getDepartments() == null) hospital.setDepartments(new HashSet<>());
            Doctor doctor = new Doctor();
            setCommonAttributes(doctor.getLoginUser(), registrationDTO);
            doctor.setFirstName(registrationDTO.getFirstName());
            doctor.setLastName(registrationDTO.getLastName());
            doctor.setMobile(registrationDTO.getMobile());
            doctor.setSpeciality(registrationDTO.getSpeciality());
            doctor.setLicenseNumber(registrationDTO.getLicenseNumber());
            doctor.setHospital(hospital);
            hospital.getDoctors().add(doctor);
            hospital.getDepartments().add(registrationDTO.getDepartment());
            doctor.setGender(registrationDTO.getGender());
            doctor.setDepartment(registrationDTO.getDepartment());
            doctorRepo.save(doctor);
            String registration = emailStructures.generateDoctorWelcomeEmail(doctor.getFullName(), hospital.getHospitalName(), registrationDTO.getUsername(), registrationDTO.getPassword());
            emailService.sendEmail(
                    doctor.getLoginUser().getEmail(),
                    "🩺 Welcome to %s, Dr. %s!".formatted(hospital.getHospitalName(), doctor.getFullName()),
                    registration
            );
        } catch (Exception e) {
            throw new HospitalManagementException("Failed to create doctor", e);
        }
    }

    @Override
    @Transactional
    public void createAdmin(RegistrationDTO registrationDTO) {
        try {
            Admin admin = new Admin();
            setCommonAttributes(admin.getLoginUser(), registrationDTO);
            admin.setFirstName(registrationDTO.getFirstName());
            admin.setLastName(registrationDTO.getLastName());
            admin.getLoginUser().setEmail(registrationDTO.getEmail());
            admin.setMobile(registrationDTO.getMobile());
            admin.setGender(registrationDTO.getGender());
            adminRepo.save(admin);
        } catch (Exception e) {
            throw new HospitalManagementException("Failed to create admin", e);
        }
    }

    @Override
    @Transactional
    public void createManager(RegistrationDTO registrationDTO) {
        try {
            Manager manager = new Manager();
            setCommonAttributes(manager.getLoginUser(), registrationDTO);
            manager.setFirstName(registrationDTO.getFirstName());
            manager.setLastName(registrationDTO.getLastName());
            manager.setGender(registrationDTO.getGender());
            manager.setMobile(registrationDTO.getMobile());
            managerRepo.save(manager);
        } catch (Exception e) {
            throw new HospitalManagementException("Failed to create manager", e);
        }
    }

    @Override
    @Transactional
    public void createPharmacist(RegistrationDTO registrationDTO) {
        try {
            Pharmacist pharmacist = new Pharmacist();
            setCommonAttributes(pharmacist.getLoginUser(), registrationDTO);
            pharmacist.setFirstName(registrationDTO.getFirstName());
            pharmacist.setLastName(registrationDTO.getLastName());
            pharmacist.setGender(registrationDTO.getGender());
            pharmacist.setMobile(registrationDTO.getMobile());
            pharmacistRepo.save(pharmacist);
        } catch (Exception e) {
            throw new HospitalManagementException("Failed to create pharmacist", e);
        }
    }

    @Override
    @Transactional
    public LoginResponse loginService(LoginRequest request) {
        LoginResponse response = new LoginResponse();
        try {
            LoginUser user = userRepo.findByUsername(request.getUsername())
                    .orElseThrow(() -> new HospitalManagementException("Invalid UserName or Password"));
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getUsername());
            String token = jwtUtils.generateToken(userDetails);
            response.setToken(token);
            response.setRole(user.getRole());
            response.setUsername(user.getUsername());
            response.setMessage("Successfully logged in");
            response.setStatus(HttpStatus.OK);
        } catch (BadCredentialsException e) {
            response.setMessage("Invalid Username or Password");
            response.setStatus(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            response.setMessage("An unexpected error occurred: " + e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    private void setCommonAttributes(LoginUser loginUser, RegistrationDTO registrationDTO) {
        try {
            loginUser.setUsername(registrationDTO.getUsername());
            loginUser.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
            loginUser.setEmail(registrationDTO.getEmail());
            loginUser.setRole(registrationDTO.getRole());
            userRepo.save(loginUser);
        } catch (Exception e) {
            throw new HospitalManagementException("Failed to create user", e);
        }
    }
}
