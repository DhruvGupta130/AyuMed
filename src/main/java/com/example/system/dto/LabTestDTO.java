package com.example.system.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class LabTestDTO {

    private Long id;
    private String testName;
    private LocalDate testDate;
    private String result;
    private String notes;
    private String filePath;
}
