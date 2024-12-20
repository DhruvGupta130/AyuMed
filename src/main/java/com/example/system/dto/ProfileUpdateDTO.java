package com.example.system.dto;

import com.example.system.entity.Address;
import com.example.system.entity.MedicalHistory;
import com.example.system.entity.Schedule;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ProfileUpdateDTO {

    //Patient
    private long AadhaarId;
    private String nationality;
    private List<MedicalHistory> medicalHistories;
    private Address address;

    //Doctor
    private LocalDate startDate;
    private String degree;
    private List<Schedule> schedule;

}
