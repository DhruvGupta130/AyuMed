package com.example.system.dto;

import com.example.system.entity.Address;
import lombok.Data;

import java.util.List;

@Data
public class HospitalDTO {

    private String hospitalName;
    private Address address;
    private String mobile;
    private String email;
    private String website;
    private int establishedYear;
    private String description;

}
