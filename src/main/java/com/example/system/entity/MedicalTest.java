package com.example.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class MedicalTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String testName; // Name of the test (e.g., "Fasting Blood Sugar")

    private LocalDate testDate; // Date when the test was conducted

    private String result; // Result of the test (e.g., "Normal", "Elevated")

    private String notes; // Any additional notes about the test or results

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private MedicalHistory history;

}
