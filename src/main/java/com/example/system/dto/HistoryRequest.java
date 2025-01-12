package com.example.system.dto;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.List;

@Data
public class HistoryRequest {

    private String problems;
    private String diagnosisDetails;
    private List<String> medications;

    @CreationTimestamp
    private LocalDate treatmentStartDate;

    @UpdateTimestamp
    private LocalDate lastTreatmentDate;

    private String treatmentPlan;
    private String followUpInstructions;
    private long patientId;
    private String notes;
}
