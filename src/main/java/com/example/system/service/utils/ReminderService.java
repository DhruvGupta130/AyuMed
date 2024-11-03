package com.example.system.service.utils;

import com.example.system.dto.EmailStructures;
import com.example.system.entity.Appointment;
import com.example.system.repository.AppointmentRepo;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
public class ReminderService {

    private final AppointmentRepo appointmentRepo;
    private final EmailService emailService;

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    @Scheduled(fixedRate = 1200000) // Check every 20 minutes
    public void sendReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime thirtyMinutesLater = now.plusMinutes(30);
        List<Appointment> appointments = appointmentRepo.findByAppointmentDateBetween(now, thirtyMinutesLater);

        for (Appointment appointment : appointments) {
            String patientEmail = appointment.getPatient().getEmail();
            String patientName = appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName();
            String appointmentDate = appointment.getAppointmentDate().format(dateFormatter);
            String appointmentTime = appointment.getAppointmentDate().format(timeFormatter);

            String doctorEmail = appointment.getDoctor().getEmail();
            String doctorName = appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName();
            String specialty = appointment.getDoctor().getSpecialty();
            String location = appointment.getDoctor().getLocation();

            // Create email
            String patientEmailBody = EmailStructures.setAppointmentReminder(patientName, doctorName, specialty, appointmentDate, appointmentTime, location);
            emailService.sendEmail(patientEmail, "Appointment Reminder - AyuMed", patientEmailBody);
        }
    }
}