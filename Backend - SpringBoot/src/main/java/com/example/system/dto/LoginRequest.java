package com.example.system.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
    private UserRole role;
}
