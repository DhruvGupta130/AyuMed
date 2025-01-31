package com.example.system.dto;

import com.example.system.entity.Address;
import com.example.system.entity.MedicalHistory;
import com.example.system.entity.Schedule;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class DoctorUpdateDTO {

    private LocalDate startDate;
    private String degree;
    private String firstName;
    private String lastName;
    private Gender gender;
    private String email;
    private long mobile;
    private String specialty;
    private String licenseNumber;
    private String department;
    private String image;

}
