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

    private String testName;
    private LocalDate testDate;
    private String result;

    private String filePath;

    @Column(length = 2000)
    private String notes;

    @ManyToOne
    @JsonIgnore
    private MedicalHistory history;

}
