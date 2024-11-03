package com.example.system.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AvailableDTO {
    private long id;
    private LocalDate date;
}
