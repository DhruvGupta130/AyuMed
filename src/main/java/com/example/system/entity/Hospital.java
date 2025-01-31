package com.example.system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

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

    @NotNull
    @ManyToOne
    private Address address;

    @Email
    @NotNull
    private String email;

    private long mobile;

    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Doctor> doctors;

    @ElementCollection
    private Set<String> departments;

    @NotNull
    @OneToOne(mappedBy = "hospital", cascade = CascadeType.ALL)
    private Manager manager;

    @NotNull
    private String website;

    @NotNull
    private Integer establishedYear;

    @NotNull
    @Column(length = 500000)
    private String description;

    @OneToMany(mappedBy = "hospital")
    private List<Feedback> feedbacks;

    @NotNull
    @ElementCollection
    private List<String> images;
}
