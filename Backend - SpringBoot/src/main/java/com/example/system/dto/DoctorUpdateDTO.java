package com.example.system.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DoctorUpdateDTO {

    private LocalDate startDate;
    private String degree;
    private String firstName;
    private String lastName;
    private Gender gender;
    private String email;
    private String mobile;
    private String speciality;
    private String licenseNumber;
    private String department;
    private String image;

}
