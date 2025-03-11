package com.example.system.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class MedicationDTO {

    private Long id;
    private String medicationName;
    private String compositionName;
    private String dosageForm;
    private String strength;
    private long quantity;
    private LocalDate expiry;
    private boolean isExpired;
    private String manufacturer;
    private double price;
    private String batchNumber;
    private String pharmacyName;
    private boolean pharmacyStatus;

}
