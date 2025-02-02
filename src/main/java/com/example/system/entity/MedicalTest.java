package com.example.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    private String testName;
    @NotNull
    private LocalDate testDate;
    @NotNull
    private String result;

    @NotNull
    @Column(length = 500)
    private String filePath;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "history_id", nullable = false)
    @JsonIgnore
    private MedicalHistory history;

}
