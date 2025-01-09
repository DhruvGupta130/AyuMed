package com.example.system.dto;

import lombok.Data;

@Data
public class FeedbackDTO {
    private long id;
    private Long appointmentId;
    private Long patientId;
    private int rating;
    private String comments;
}
