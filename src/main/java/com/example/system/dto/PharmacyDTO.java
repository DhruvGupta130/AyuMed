package com.example.system.dto;

import com.example.system.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PharmacyDTO {

    private long id;
    private String pharmacyName;
    private Address address;
    private String mobile;
    private String email;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private List<String> images;
    private boolean open = true;
}
