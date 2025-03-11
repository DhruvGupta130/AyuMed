package com.example.system.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class HospitalPatientDTO {
    private long id;
    private String fullName;
    private String email;
    private String mobile;
    private LocalDate dateOfBirth;
    private Gender gender;

    private long doctorId;
    private String doctorName;
    private String department;
}