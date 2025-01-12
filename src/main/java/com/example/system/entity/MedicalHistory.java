package com.example.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
    @CreationTimestamp
    private LocalDate treatmentStartDate;

    @PastOrPresent
    @UpdateTimestamp
    private LocalDate lastTreatmentDate;

    private String treatmentPlan;
    private String followUpInstructions;

    @OneToMany(mappedBy = "history", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MedicalTest> testsConducted;

    @ManyToOne
    @JsonIgnore
    private Patient patient;

    @ManyToOne
    @JsonIgnore
    private Doctor doctor;

    private String notes;
}
