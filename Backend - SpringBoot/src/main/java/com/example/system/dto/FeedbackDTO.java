package com.example.system.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class FeedbackDTO {
    private long id;
    private int rating;
    private String comments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private PatientDTO patient;
    private DoctorDTO doctor;
}
