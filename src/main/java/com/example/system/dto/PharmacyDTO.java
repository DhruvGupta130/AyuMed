package com.example.system.dto;

import com.example.system.entity.Address;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
public class PharmacyDTO {

    private long id;
    private String pharmacyName;
    private String overview;
    private String services;
    private String pharmacyTechnology;
    private String accreditations;
    private String insurancePartners;
    private Address address;
    private String email;
    private String mobile;
    private String website;
    private boolean open;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private List<String> images;
}
