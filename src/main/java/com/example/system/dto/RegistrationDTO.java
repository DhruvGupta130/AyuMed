package com.example.system.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RegistrationDTO {

    //Authentication
    private String username;
    private String password;
    private UserRole role;

    //Common
    private String firstName;
    private String lastName;
    private Gender gender;
    private String email;
    private String Mobile;
    private String department;

    // For doctors
    private String specialty;
    private String licenseNumber;
    private long hospitalId;

    // For patients
    private LocalDate dateOfBirth;

}
