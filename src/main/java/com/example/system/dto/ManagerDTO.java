package com.example.system.dto;

import com.example.system.entity.Hospital;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ManagerDTO {
    private long id;
    private String firstName;
    private String lastName;
    private Gender gender;
    private String email;
    private long mobile;
    private String fullName;
}
