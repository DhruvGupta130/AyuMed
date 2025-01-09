package com.example.system.dto;

import com.example.system.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
public class PharmacyDTO {

    private long id;
    private String pharmacyName;
    private Address address;
    private String email;
    private Long mobile;
    private boolean open;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private List<String> images;
}
