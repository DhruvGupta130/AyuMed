package com.example.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class PatientRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Patient patient;

    private String fileName;
    private String publicId;
    private String fileUrl;
    private Date uploadDate;
    private String fileType;

    @Column(length = 1000)
    private String description;

}

