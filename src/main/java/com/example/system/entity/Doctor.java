package com.example.system.entity;

import com.example.system.dto.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

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

    @Email
    private String email;
    private long mobile;

    private String specialty;
    private String licenseNumber;
    private String department;

    private LocalDate startDate;
    private String image;
    private String degree;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Schedule> schedules;

    @JsonIgnore
    @ManyToOne
    private Hospital hospital;

    @NotNull
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
        if (startDate == null) {
            return 0;
        }
        return (int) ChronoUnit.YEARS.between(startDate, LocalDate.now());
    }

    public String getLocation() {
        return "%s, %s, %s, %s, %s, %s".formatted(hospital.getHospitalName(), hospital.getAddress().getStreet(), hospital.getAddress().getCity(), hospital.getAddress().getState(), hospital.getAddress().getZip(), hospital.getAddress().getCountry());
    }
}
