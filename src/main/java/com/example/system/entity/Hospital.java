package com.example.system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String hospitalName;

    @Email
    @NotNull
    private String email;

    @Pattern(regexp = "^[+]?[0-9\\- ]{7,20}$", message = "Invalid phone number")
    private String mobile;

    @NotNull
    @Pattern(regexp = "^(https?://)?([a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,10}(/.*)?$", message = "Invalid website URL")
    private String website;

    @NotNull
    private Integer establishedYear;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String overview;

    @ElementCollection
    private List<String> specialities;

    private boolean emergencyServices = false;

    @Column(columnDefinition = "TEXT")
    private String technology;

    private int bedCapacity;
    private int icuCapacity;
    private int operationTheaters;

    @Column(columnDefinition = "TEXT")
    private String accreditations;

    @ElementCollection
    private List<String> insurancePartners;

    @ElementCollection
    @CollectionTable(name = "hospital_departments", joinColumns = @JoinColumn(name = "hospital_id"))
    private Set<String> departments;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    private Address address;

    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Doctor> doctors;

    @NotNull
    @OneToOne(mappedBy = "hospital", cascade = CascadeType.ALL)
    private Manager manager;

    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Feedback> feedbacks;

    @ElementCollection
    @CollectionTable(name = "hospital_images", joinColumns = @JoinColumn(name = "hospital_id"))
    private List<String> images = new ArrayList<>();
}