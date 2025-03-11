package com.example.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class MedicalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(length = 500)
    private String problems;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String diagnosisDetails;

    @ElementCollection
    @CollectionTable(name = "medical_history_medications", joinColumns = @JoinColumn(name = "history_id"))
    @Column(name = "medication")
    private List<String> medications;

    @PastOrPresent
    @CreationTimestamp
    private LocalDateTime treatmentStartDate;

    @PastOrPresent
    @UpdateTimestamp
    private LocalDateTime lastTreatmentDate;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String treatmentPlan;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String followUpInstructions;

    @OneToMany(mappedBy = "history", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<MedicalTest> testsConducted;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    @JsonIgnore
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    @JsonIgnore
    private Doctor doctor;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String notes;
}