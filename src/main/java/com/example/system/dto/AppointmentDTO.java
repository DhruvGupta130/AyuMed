package com.example.system.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class AppointmentDTO {

    private Long id;
    private PatientDTO patient;
    private DoctorDTO doctor;
    private LocalDateTime appointmentDate;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;
    private AppointmentStatus status;
    private String cancellationReason;
    private String createdBy;
    private String lastModifiedBy;
    private int slotIndex;

}
