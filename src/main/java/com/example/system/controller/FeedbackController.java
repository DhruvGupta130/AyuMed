package com.example.system.controller;

import com.example.system.dto.FeedbackDTO;
import com.example.system.entity.Feedback;
import com.example.system.service.FeedbackService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping("/submit")
    public ResponseEntity<Feedback> submitFeedback(@RequestBody FeedbackDTO feedbackDTO) {
        Feedback feedback = feedbackService.submitFeedback(feedbackDTO.getAppointmentId(),
                feedbackDTO.getPatientId(), feedbackDTO.getRating(), feedbackDTO.getComments());
        return ResponseEntity.ok(feedback);
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<List<Feedback>> getFeedbackByAppointment(@PathVariable Long appointmentId) {
        List<Feedback> feedbackList = feedbackService.getFeedbackByAppointmentId(appointmentId);
        return ResponseEntity.ok(feedbackList);
    }
}
