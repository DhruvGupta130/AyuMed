package com.example.system.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AdminDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private Gender gender;
    private String email;
    private long mobile;
    private String fullName;
}
