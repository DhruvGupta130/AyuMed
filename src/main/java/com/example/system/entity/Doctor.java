package com.example.system.entity;

import com.example.system.dto.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Setter
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "^[+]?[0-9\\- ]{7,20}$", message = "Invalid phone number")
    private String mobile;

    private String specialty;

    @Pattern(regexp = "^[A-Z0-9]{5,15}$", message = "Invalid license number format")
    private String licenseNumber;

    private String department;

    private LocalDate startDate;
    private String image;

    @Pattern(regexp = "^[a-zA-Z.\\s]+$", message = "Degree should contain only letters, spaces, and periods")
    private String degree;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Schedule> schedules;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MedicalHistory> medicalHistories;

    @JsonIgnore
    @ManyToOne
    private Hospital hospital;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LoginUser loginUser = new LoginUser();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Appointment> appointments;

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public int getExperience() {
        return (startDate == null) ? 0 : (int) ChronoUnit.YEARS.between(startDate, LocalDate.now());
    }

    public String getLocation() {
        return Optional.ofNullable(hospital)
                .map(h -> "%s, %s, %s, %s, %s, %s".formatted(
                        h.getHospitalName(),
                        Optional.ofNullable(h.getAddress()).map(Address::getStreet).orElse(""),
                        Optional.ofNullable(h.getAddress()).map(Address::getCity).orElse(""),
                        Optional.ofNullable(h.getAddress()).map(Address::getState).orElse(""),
                        Optional.ofNullable(h.getAddress()).map(Address::getZip).orElse(""),
                        Optional.ofNullable(h.getAddress()).map(Address::getCountry).orElse("")
                ))
                .orElse("Hospital details unavailable");
    }
}
