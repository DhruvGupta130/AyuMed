package com.example.system.dto;

import com.example.system.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HospitalDTO {

    private String hospitalName;
    private Address address;
    private String mobile;
    private String email;
    private String website;
    private int establishedYear;
    private String description;

}
