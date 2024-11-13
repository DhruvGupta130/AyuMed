package com.example.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
public class MedicalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String problems;
    private String diagnosisDetails;

    @ElementCollection
    private List<String> medications;

    @PastOrPresent
    private LocalDate treatmentStartDate;

    @PastOrPresent
    private LocalDate treatmentEndDate;

    private String treatmentPlan;
    private String followUpInstructions;

    @OneToMany(mappedBy = "history", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MedicalTest> testsConducted;

    @ManyToOne
    @JsonIgnore
    private Patient patient;

    private String notes;
}
