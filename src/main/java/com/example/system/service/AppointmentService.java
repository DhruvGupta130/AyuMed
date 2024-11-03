package com.example.system.service;

import com.example.system.dto.AppointmentStatus;
import com.example.system.entity.Appointment;
import com.example.system.entity.Doctor;
import com.example.system.entity.Patient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface AppointmentService {

    List<Appointment> getAllAppointments();
    Appointment getAppointmentById(Long id);
    void scheduleAppointment(Patient patient, Doctor doctor, LocalDate appointmentDate, String createdBy, int slotIndex);
    void UpdateAppointmentStatus(Appointment appointment, AppointmentStatus status);
    void cancelAppointment(Long id, String cancellationReason, String modifiedBy);
    boolean removeOldCanceledAppointments(Patient patient, Appointment appointment);
    List<Appointment> filterAppointments(LocalDate startDate, LocalDate endDate, AppointmentStatus status, Long doctorId);
}
