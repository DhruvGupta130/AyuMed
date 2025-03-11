package com.example.system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Pharmacy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String pharmacyName;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String overview;

    @Column(columnDefinition = "TEXT")
    private String services;

    @Column(columnDefinition = "TEXT")
    private String pharmacyTechnology;

    @Column(columnDefinition = "TEXT")
    private String accreditations;

    @ElementCollection
    private List<String> insurancePartners;

    @ManyToOne(cascade = CascadeType.ALL)
    private Address address;

    @Email
    private String email;

    @Pattern(regexp = "^[+]?[0-9\\- ]{7,20}$", message = "Invalid phone number")
    private String mobile;

    @Pattern(regexp = "^(https?://)?([a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,10}(/.*)?$", message = "Invalid website URL")
    private String website;

    @NotNull
    private LocalTime openingTime;

    @NotNull
    private LocalTime closingTime;

    private boolean open;

    @OneToMany(mappedBy = "pharmacy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Medication> medications = new ArrayList<>();

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    private Pharmacist pharmacist;

    @ElementCollection
    @CollectionTable(name = "pharmacy_images", joinColumns = @JoinColumn(name = "pharmacy_id"))
    private List<String> images = new ArrayList<>();
}
