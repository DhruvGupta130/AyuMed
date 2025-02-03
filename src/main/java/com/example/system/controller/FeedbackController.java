package com.example.system.controller;

import com.example.system.dto.FeedbackDTO;
import com.example.system.dto.Response;
import com.example.system.entity.Doctor;
import com.example.system.entity.Feedback;
import com.example.system.entity.Manager;
import com.example.system.entity.Patient;
import com.example.system.exception.HospitalManagementException;
import com.example.system.service.FeedbackService;
import com.example.system.service.utils.Utility;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final Utility utility;

    @PostMapping("/patient/feedback/{appointmentId}")
    public ResponseEntity<Response> submitFeedback(@RequestHeader("Authorization") String token,
                                                   @RequestBody Feedback feedback,
                                                   @PathVariable long appointmentId) {
        Response response = new Response();
        try {
            Patient patient = (Patient) utility.getUserFromToken(token);
            feedbackService.submitFeedback(appointmentId, feedback, patient);
            response.setMessage("Feedback submitted successfully!");
            response.setStatus(HttpStatus.CREATED);
        } catch (HospitalManagementException e){
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            response.setMessage("Error submitting feedback" + e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/patient/feedback/{appointmentId}")
    public ResponseEntity<FeedbackDTO> getFeedback(@RequestHeader("Authorization") String token,
                                                   @PathVariable long appointmentId) {
        Patient patient = (Patient) utility.getUserFromToken(token);
        return ResponseEntity.ok(feedbackService.getPatientFeedback(patient, appointmentId));
    }

    @GetMapping("doctor/feedback")
    public ResponseEntity<List<FeedbackDTO>> getFeedback(@RequestHeader("Authorization") String token) {
        Doctor doctor = (Doctor) utility.getUserFromToken(token);
        return ResponseEntity.ok(feedbackService.getDoctorFeedback(doctor));
    }

    @GetMapping("/hospital/feedbacks")
    public ResponseEntity<List<FeedbackDTO>> getHospitalFeedback(@RequestHeader("Authorization") String token) {
        Manager manager = (Manager) utility.getUserFromToken(token);
        return ResponseEntity.ok(feedbackService.getHospitalFeedbacks(manager.getHospital()));
    }
}
