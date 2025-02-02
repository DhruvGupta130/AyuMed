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
    private String doctorName;
    private String doctorDepartment;
    private String doctorImage;
    private String patientName;
    private String patientImage;
    private Gender patientGender;
    private String patientMobile;
    private String patientEmail;
    private String hospitalName;
    private String hospitalAddress;
    private LocalDateTime appointmentDate;
    private AppointmentStatus status;
    private String cancellationReason;
    private int slotIndex;

}
