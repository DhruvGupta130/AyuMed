package com.example.system.dto;

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
    private String mobile;
    private String hospitalName;
    private String fullName;
}
