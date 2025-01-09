package com.example.system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
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

    @ManyToOne
    private Address address;

    @Email
    private String email;

    private long mobile;

    @OneToMany(mappedBy = "pharmacy", cascade = CascadeType.ALL)
    private List<Medication> medications;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    private Pharmacist pharmacist;

    private boolean open;

    @NotNull
    private LocalTime openingTime;
    @NotNull
    private LocalTime closingTime;

    @ElementCollection
    private List<String> images = new ArrayList<>();
}
