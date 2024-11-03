package com.example.system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DoctorDTO {

    private String firstName;
    private String lastName;
    private String specialty;
    private String department;
    private Integer experience;
    private String image;
    private String degree;
}
