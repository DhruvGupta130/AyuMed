package com.example.system.service;

import com.example.system.entity.Feedback;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FeedbackService{

    Feedback submitFeedback(Long appointmentId, Long patientId, int rating, String comments);
    List<Feedback> getFeedbackByAppointmentId(Long appointmentId);
}
