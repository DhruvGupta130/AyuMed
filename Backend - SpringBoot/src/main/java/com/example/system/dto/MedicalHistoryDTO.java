package com.example.system.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MedicalHistoryDTO {

    private Long id;
    private String problems;
    private String diagnosisDetails;
    private List<String> medications;
    private LocalDate treatmentStartDate;
    private LocalDate lastTreatmentDate;
    private String treatmentPlan;
    private String followUpInstructions;
    private String patientName;
    private String doctorName;
    private String notes;
}
