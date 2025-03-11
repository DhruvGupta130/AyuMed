package com.example.system.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RegistrationDTO {

    //Authentication
    private String username;
    private String password;
    private UserRole role;
    private String otp;

    //Common
    private String firstName;
    private String lastName;
    private Gender gender;
    private String email;
    private String mobile;

    // For doctors
    private String department;
    private String speciality;
    private String licenseNumber;
    private long hospitalId;

    // For patients
    private LocalDate dateOfBirth;

}
