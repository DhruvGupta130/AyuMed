package com.example.system.dto;

import com.example.system.entity.Address;
import com.example.system.entity.Appointment;
import com.example.system.entity.MedicalHistory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class PatientDTO {

    private long id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String email;
    private Long mobile;
    private Long alternateMobile;
    private Long aadhaarId;
    private String nationality;
    private String image;
    private String fullName;
    private Address address;
}
