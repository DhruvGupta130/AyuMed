package com.example.system.service.Impl;

import com.example.system.entity.Feedback;
import com.example.system.entity.Appointment;
import com.example.system.exception.HospitalManagementException;
import com.example.system.repository.AppointmentRepo;
import com.example.system.repository.FeedbackRepo;
import com.example.system.service.FeedbackService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepo feedbackRepo;
    private final AppointmentRepo appointmentRepo;

    @Transactional
    public Feedback submitFeedback(Long appointmentId, Long patientId, int rating, String comments) {
        Appointment appointment = appointmentRepo.findById(appointmentId)
                .orElseThrow(() -> new HospitalManagementException("Appointment not found"));

        Feedback feedback = new Feedback();
        feedback.setAppointment(appointment);
        feedback.setPatientId(patientId);
        feedback.setRating(rating);
        feedback.setComments(comments);

        return feedbackRepo.save(feedback);
    }

    public List<Feedback> getFeedbackByAppointmentId(Long appointmentId) {
        return feedbackRepo.findByAppointmentId(appointmentId); // Retrieve feedback for the specified appointment
    }
}
