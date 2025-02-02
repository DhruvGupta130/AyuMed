package com.example.system.entity;

import com.example.system.dto.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@ToString
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String firstName;
    @NotNull
    private String lastName;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "^[+]?[0-9\\- ]{7,20}$", message = "Invalid phone number")
    private String mobile;

    @Pattern(regexp = "^[+]?[0-9\\- ]{7,20}$", message = "Invalid phone number")
    private String alternateMobile;

    @JsonIgnore
    @Pattern(regexp = "^\\d{12}$", message = "Aadhaar ID must be a 12-digit number")
    private String aadhaarId;

    private String image;

    @JsonIgnore
    @OneToMany(mappedBy = "patient", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<MedicalHistory> medicalHistories;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Address address;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LoginUser loginUser = new LoginUser();

    @JsonIgnore
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Appointment> appointments;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient")
    private List<PatientRecord> patientRecords;

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
