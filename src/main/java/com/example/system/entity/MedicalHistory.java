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

    private String problems; // E.g., "Diabetes", "Hypertension", etc.

    private String diagnosisDetails; // Details about the diagnosis, if any.

    @ElementCollection
    private List<String> medications; // List of medications prescribed.

    @PastOrPresent
    private LocalDate treatmentStartDate; // Start date of the treatment.

    @PastOrPresent
    private LocalDate treatmentEndDate;   // End date of the treatment, if applicable.

    private String treatmentPlan; // Description of the treatment plan.
    private String followUpInstructions; // Instructions for follow-up care.

    @OneToMany(mappedBy = "history", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MedicalTest> testsConducted; // List of tests conducted relevant to the condition.

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private Patient patient;

    private String notes;     // Any additional notes regarding the condition.
}
