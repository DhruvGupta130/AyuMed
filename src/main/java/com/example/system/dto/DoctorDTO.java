package com.example.system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DoctorDTO {

    private long id;
    private String firstName;
    private String lastName;
    private Gender gender;
    private String email;
    private String mobile;
    private String specialty;
    private String licenseNumber;
    private String department;
    private Integer experience;
    private LocalDate startDate;
    private String image;
    private String degree;
    private String fullName;
}
