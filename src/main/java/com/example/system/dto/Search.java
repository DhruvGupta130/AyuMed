package com.example.system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Search {

    List<HospitalDTO> hospitals;
    List<DoctorDTO> doctors;
}
