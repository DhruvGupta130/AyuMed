package com.example.system.dto;

import com.example.system.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
public class HospitalDTO {

    private long id;
    private String hospitalName;
    private Address address;
    private String email;
    private long mobile;
    private Set<String> departments;
    private String website;
    private int establishedYear;
    private String description;
    private List<String> images;

}
