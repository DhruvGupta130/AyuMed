package com.example.system.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class PharmacyPatientsDTO {
    private long id;
    private String fullName;
    private String email;
    private String mobile;
    private LocalDate dateOfBirth;
    private Gender gender;

    private long medicationId;
    private String medicationName;
    private long quantity;
    private double cost;
}
