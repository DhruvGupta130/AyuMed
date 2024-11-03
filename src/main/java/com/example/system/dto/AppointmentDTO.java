package com.example.system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AppointmentDTO {

    private PatientDTO patient;
    private DoctorDTO doctor;
    private LocalDateTime appointmentDate;
    private AppointmentStatus status;

}
