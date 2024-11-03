package com.example.system.entity;

import jakarta.persistence.*;
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
    private String HospitalName;

    @ManyToOne
    private Address address;

    private String mobile;
    private String email;

    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Doctor> doctors;

    @ElementCollection
    private Set<String> departments;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "manager_id")
    private HospitalManager manager;

    private String website;
    private int establishedYear;

    @Column(length = 500)
    private String description;

}
