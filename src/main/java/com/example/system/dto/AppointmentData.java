package com.example.system.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AppointmentData {

    private Long id;
    private Long patientId;
    private Long doctorId;
    private int slot;
    private LocalDate appointmentDate;
    private AppointmentStatus status;
    private String cancellationReason;
}
