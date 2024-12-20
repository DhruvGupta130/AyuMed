package com.example.system.entity;

import com.example.system.dto.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Past
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Email
    private String email;
    private Long mobile;
    private Long alternateMobile;

    private Long aadhaarId;

    private String nationality;
    private String image;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MedicalHistory> medicalHistories;

    @ManyToOne
    private Address address;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LoginUser loginUser = new LoginUser();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Appointment> appointments;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient")
    private List<PatientRecord> patientRecords;

    public String getFullName() {
        return firstName + " " + lastName;
    }

}
