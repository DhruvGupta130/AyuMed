package com.example.system.controller;

import com.example.system.dto.AppointmentData;
import com.example.system.dto.AppointmentStatus;
import com.example.system.entity.*;
import com.example.system.entity.Doctor;
import com.example.system.entity.Patient;
import com.example.system.exception.HospitalManagementException;
import com.example.system.repository.AppointmentRepo;
import com.example.system.repository.DoctorRepo;
import com.example.system.service.AppointmentService;
import com.example.system.service.utils.Utility;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class AppointmentController {

    private final Utility utility;
    private final AppointmentRepo appointmentRepo;
    private final DoctorRepo doctorRepo;
    private final AppointmentService appointmentService;

    @Transactional
    @PostMapping("/patient/appointment")
    public ResponseEntity<String> scheduleAppointment(@RequestHeader("Authorization") String token,
                                                    @RequestBody AppointmentData appointmentData) {
        Object user = utility.getUserFromToken(token);
        if(user instanceof Patient patient) {
            Doctor doctor = doctorRepo.getDoctorById(appointmentData.getDoctorId()).orElseThrow(
                    ()-> new HospitalManagementException("Doctor not found"));
            appointmentService.scheduleAppointment(patient, doctor, appointmentData.getAppointmentDate(),
                    patient.getFirstName() + " " + patient.getLastName(), appointmentData.getSlot());
            return new ResponseEntity<>("Appointment scheduled Successfully", HttpStatus.OK);
        }
        throw new HospitalManagementException("Patient not found");
    }

    @PutMapping("/doctor/appointment")
    public ResponseEntity<String> updateAppointmentStatus(@RequestHeader("Authorization") String token,
                                                          @RequestBody AppointmentData appointmentData) {
        Object user = utility.getUserFromToken(token);
        if(user instanceof Doctor doctor) {
            Appointment appointment = appointmentRepo.findById(appointmentData.getId()).orElseThrow(
                    () -> new HospitalManagementException("Appointment not found"));
            if(doctor.getAppointments().stream().noneMatch(a -> a.equals(appointment))) throw new HospitalManagementException("You are not authorized to access this appointment.");
            if (!appointment.getDoctor().getId().equals(doctor.getId()))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to access this appointment.");
            appointmentService.UpdateAppointmentStatus(appointment, appointmentData.getStatus());
            return ResponseEntity.ok("Status Updated Successfully");
        }
        throw new HospitalManagementException("Doctor not found");
    }

    @PutMapping("/doctor/appointment/cancel")
    public ResponseEntity<String> cancelAppointmentDoctor(@RequestHeader("Authorization") String token,
                                                          @RequestBody AppointmentData appointmentData){
        Object user = utility.getUserFromToken(token);
        if(user instanceof Doctor doctor) {
            if(doctor.getAppointments().stream().noneMatch(appointment -> appointment.getId().equals(appointmentData.getId()))) throw new HospitalManagementException("You are not authorized to access this appointment.");
            appointmentService.cancelAppointment(appointmentData.getId(), appointmentData.getCancellationReason(),
                    doctor.getFirstName() + " " + doctor.getLastName());
            return ResponseEntity.ok("Appointment Cancelled Successfully");
        }
        throw new HospitalManagementException("Doctor not found");
    }

    @PutMapping("/admin/appointment/cancel")
    public ResponseEntity<String> cancelAppointmentAdmin(@RequestHeader("Authorization") String token,
                                                         @RequestBody AppointmentData appointmentData){
        Object user = utility.getUserFromToken(token);
        if(user instanceof Admin admin) {
            appointmentService.cancelAppointment(appointmentData.getId(), appointmentData.getCancellationReason(),
                    admin.getFirstName() + " " + admin.getLastName());
            return ResponseEntity.ok("Appointment Cancelled Successfully");
        }
        throw new HospitalManagementException("Admin not found");
    }

    @PutMapping("/patient/appointment/cancel")
    public ResponseEntity<String> cancelAppointmentPatient(@RequestHeader("Authorization") String token,
                                                           @RequestBody AppointmentData appointmentData){
        Object user = utility.getUserFromToken(token);
        if(user instanceof Patient patient) {
            if(patient.getAppointments().stream().noneMatch(appointment -> appointment.getId().equals(appointmentData.getId()))) throw new HospitalManagementException("You are not authorized to access this appointment.");
            appointmentService.cancelAppointment(appointmentData.getId(), appointmentData.getCancellationReason(),
                    patient.getFirstName() + " " + patient.getLastName());
            return ResponseEntity.ok("Appointment Cancelled Successfully");
        }
        throw new HospitalManagementException("Admin not found");
    }

    @GetMapping("/doctor/appointment")
    public ResponseEntity<List<Appointment>> getDoctorAppointments(@RequestHeader("Authorization") String token) {
        Object user = utility.getUserFromToken(token);
        if(user instanceof Doctor doctor) {
            List<Appointment> appointments = appointmentRepo.findAllByDoctor(doctor);
            if(appointments == null) appointments = List.of();
            return ResponseEntity.ok(appointments);
        }
        throw new HospitalManagementException("Doctor not found");
    }

    @GetMapping("/patient/appointment")
    public ResponseEntity<List<Appointment>> getPatientAppointments(@RequestHeader("Authorization") String token) {
        Object user = utility.getUserFromToken(token);
        if(user instanceof Patient patient) {
            List<Appointment> appointments = appointmentRepo.findAllByPatient(patient);
            if(appointments == null) appointments = List.of();
            return ResponseEntity.ok(appointments);
        }
        throw new HospitalManagementException("Patient not found");
    }

    @DeleteMapping("/patient/appointment/delete")
    public ResponseEntity<String> removeAppointment(@RequestHeader("Authorization") String token,
                                                    @RequestBody AppointmentData appointmentData) {
        Object user = utility.getUserFromToken(token);
        if(user instanceof Patient patient) {
            Appointment appointment = appointmentRepo.findById(appointmentData.getId()).orElseThrow(
                    () -> new HospitalManagementException("Appointment not found"));
             if(appointmentService.removeOldCanceledAppointments(patient, appointment))
                 return ResponseEntity.ok("Appointment Deleted Successfully");
             return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Appointment removal failed");
        }
        throw new HospitalManagementException("Patient not found");
    }

    @GetMapping("/admin/appointments/filter")
    public ResponseEntity<List<Appointment>> filterAppointments(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) AppointmentStatus status,
            @RequestParam(required = false) Long doctorId) {
        List<Appointment> appointments = appointmentService.filterAppointments(startDate, endDate, status, doctorId);
        return ResponseEntity.ok(appointments);
    }

}
