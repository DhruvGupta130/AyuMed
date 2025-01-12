package com.example.system.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class LabTestRequest {

    private String testName;
    private LocalDate testDate;
    private String result;
    private String notes;
    private long historyId;
    private MultipartFile file;
}
