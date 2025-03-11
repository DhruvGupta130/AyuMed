package com.example.system.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String street;
    @NotNull
    private String city;
    @NotNull
    private String state;
    @NotNull
    private String zip;
    @NotNull
    private String country;
    @NotNull
    private double latitude;
    @NotNull
    private double longitude;

    @Override
    public String toString() {
        return
                street + ", " +
                city + ", " +
                state + ", " +
                zip + ", " +
                country;
    }
}