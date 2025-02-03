package com.example.system.service;

import com.example.system.dto.FeedbackDTO;
import com.example.system.entity.Doctor;
import com.example.system.entity.Feedback;
import com.example.system.entity.Hospital;
import com.example.system.entity.Patient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FeedbackService{

    FeedbackDTO getPatientFeedback(Patient patient, long appointmentId);
    void submitFeedback(long appointmentId, Feedback feedback, Patient patient);
    List<FeedbackDTO> getDoctorFeedback(Doctor doctor);
    List<FeedbackDTO> getHospitalFeedbacks(Hospital hospital);
}
