package com.example.system.service.impl;

import com.example.system.dto.AppointmentStatus;
import com.example.system.dto.FeedbackDTO;
import com.example.system.entity.*;
import com.example.system.exception.HospitalManagementException;
import com.example.system.repository.AppointmentRepo;
import com.example.system.repository.FeedbackRepo;
import com.example.system.service.DoctorService;
import com.example.system.service.FeedbackService;
import com.example.system.service.PatientService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepo feedbackRepo;
    private final AppointmentRepo appointmentRepo;
    private final PatientService patientService;
    private final DoctorService doctorService;

    @Override
    public FeedbackDTO getPatientFeedback(Patient patient, long appointmentId) {
        Appointment appointment = appointmentRepo.findById(appointmentId)
                .orElseThrow(() -> new HospitalManagementException("Appointment not found"));
        if(!appointment.getPatient().equals(patient)) throw new HospitalManagementException("You are not authorized to perform this operation");
        return getFeedback(feedbackRepo.findByAppointmentId(appointmentId));
    }

    @Transactional
    public void submitFeedback(long appointmentId, Feedback feedback, Patient patient) {
        Appointment appointment = appointmentRepo.findById(appointmentId)
                .orElseThrow(() -> new HospitalManagementException("Appointment not found"));
        if(!appointment.getPatient().equals(patient)) throw new HospitalManagementException("You are not authorized to perform this operation");
        if(!appointment.getStatus().equals(AppointmentStatus.COMPLETED)) throw new HospitalManagementException("Sorry! your appointment is not completed");
        feedback.setAppointment(appointment);
        feedbackRepo.save(feedback);
    }

    @Override
    public List<FeedbackDTO> getDoctorFeedback(Doctor doctor) {
        return feedbackRepo.getFeedbacksByDoctor(doctor).stream().map(this::getFeedback).toList();
    }

    @Override
    public List<FeedbackDTO> getHospitalFeedbacks(Hospital hospital) {
        if(hospital == null) throw new HospitalManagementException("Hospital not found");
        return feedbackRepo.getFeedbacksByHospital(hospital).stream().map(this::getFeedback).toList();
    }

    private FeedbackDTO getFeedback(Feedback feedback) {
        return new FeedbackDTO(
                feedback.getId(), feedback.getRating(),
                feedback.getComments(), feedback.getCreatedAt(),
                feedback.getUpdatedAt(),
                patientService.getPatientProfile(feedback.getAppointment().getPatient()),
                doctorService.getDoctorProfile(feedback.getAppointment().getDoctor())
        );
    }
}
