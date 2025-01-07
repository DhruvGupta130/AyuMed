package com.example.system.service.impl;

import com.example.system.configuration.JwtUtils;
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
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
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

    @Override
    @Transactional
    public void createPatient(RegistrationDTO registrationDTO) {
        Patient patient = new Patient();
        setCommonAttributes(patient.getLoginUser(), registrationDTO);
        patient.setFirstName(registrationDTO.getFirstName());
        patient.setLastName(registrationDTO.getLastName());
        patient.setDateOfBirth(registrationDTO.getDateOfBirth());
        patient.setGender(registrationDTO.getGender());
        patient.setEmail(registrationDTO.getEmail());
        patient.setMobile(registrationDTO.getMobile());
        patientRepo.save(patient);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('MANAGEMENT')")
    public void createDoctor(RegistrationDTO registrationDTO) {
        Hospital hospital = hospitalRepo.findById(registrationDTO.getHospitalId()).orElseThrow(
                ()-> new HospitalManagementException("No Hospital found"));
        if(hospital.getDoctors()==null) hospital.setDoctors(new ArrayList<>());
        if(hospital.getDepartments()==null) hospital.setDepartments(new HashSet<>());
        Doctor doctor = new Doctor();
        setCommonAttributes(doctor.getLoginUser(), registrationDTO);
        doctor.setFirstName(registrationDTO.getFirstName());
        doctor.setLastName(registrationDTO.getLastName());
        doctor.setEmail(registrationDTO.getEmail());
        doctor.setMobile(registrationDTO.getMobile());
        doctor.setSpecialty(registrationDTO.getSpecialty());
        doctor.setLicenseNumber(registrationDTO.getLicenseNumber());
        doctor.setHospital(hospital);
        hospital.getDoctors().add(doctor);
        hospital.getDepartments().add(registrationDTO.getDepartment());
        doctor.setGender(registrationDTO.getGender());
        doctor.setDepartment(registrationDTO.getDepartment());
        doctorRepo.save(doctor);
    }

    @Override
    @Transactional
    public void createAdmin(RegistrationDTO registrationDTO) {
        Admin admin = new Admin();
        setCommonAttributes(admin.getLoginUser(), registrationDTO);
        admin.setFirstName(registrationDTO.getFirstName());
        admin.setLastName(registrationDTO.getLastName());
        admin.setEmail(registrationDTO.getEmail());
        admin.setMobile(registrationDTO.getMobile());
        admin.setGender(registrationDTO.getGender());
        adminRepo.save(admin);
    }

    @Override
    @Transactional
    public void createManager(RegistrationDTO registrationDTO) {
        Manager manager = new Manager();
        setCommonAttributes(manager.getLoginUser(), registrationDTO);
        manager.setFirstName(registrationDTO.getFirstName());
        manager.setLastName(registrationDTO.getLastName());
        manager.setGender(registrationDTO.getGender());
        manager.setEmail(registrationDTO.getEmail());
        manager.setMobile(registrationDTO.getMobile());
        managerRepo.save(manager);
    }

    @Override
    @Transactional
    public void createPharmacist(RegistrationDTO registrationDTO) {
        Pharmacist pharmacist = new Pharmacist();
        setCommonAttributes(pharmacist.getLoginUser(), registrationDTO);
        pharmacist.setFirstName(registrationDTO.getFirstName());
        pharmacist.setLastName(registrationDTO.getLastName());
        pharmacist.setEmail(registrationDTO.getEmail());
        pharmacist.setGender(registrationDTO.getGender());
        pharmacist.setMobile(registrationDTO.getMobile());
        pharmacistRepo.save(pharmacist);
    }

    @Override
    @Transactional
    public LoginResponse loginService(LoginRequest request) {
        LoginUser user = userRepo.findByUsername(request.getUsername()).orElseThrow(() -> new HospitalManagementException("Invalid UserName or Password"));
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtUtils.generateToken(userDetails);
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUsername(user.getUsername());
        response.setRole(user.getRole().name());
        response.setMessage("Successfully logged in");
        return response;
    }

    private void setCommonAttributes(LoginUser loginUser, RegistrationDTO registrationDTO) {
        loginUser.setUsername(registrationDTO.getUsername());
        loginUser.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        loginUser.setRole(registrationDTO.getRole());
        userRepo.save(loginUser);
    }
}
