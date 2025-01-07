package com.example.system.controller;

import com.example.system.dto.AppointmentData;
import com.example.system.dto.AppointmentStatus;
import com.example.system.dto.Response;
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
    public ResponseEntity<Response> scheduleAppointment(@RequestHeader("Authorization") String token,
                                                        @RequestBody AppointmentData appointmentData) {
        Response response = new Response();
        try {
            Patient patient = (Patient) utility.getUserFromToken(token);
            if(patient == null) throw new HospitalManagementException("Patient not found.");
            Doctor doctor = doctorRepo.getDoctorById(appointmentData.getDoctorId()).orElseThrow(
                    () -> new HospitalManagementException("Doctor not found"));
            appointmentService.scheduleAppointment(patient, doctor, appointmentData.getAppointmentDate(),
                    patient.getFirstName() + " " + patient.getLastName(), appointmentData.getSlot());
            response.setMessage("Appointment scheduled Successfully");
            response.setStatus(HttpStatus.OK);
        } catch (HospitalManagementException e) {
            response.setError(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.setError("Error while scheduling appointment: "+ e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/patient/appointment/cancel")
    public ResponseEntity<Response> cancelAppointmentPatient(@RequestHeader("Authorization") String token,
                                                             @RequestBody AppointmentData appointmentData){
        Response response = new Response();
        try {
            Patient patient = (Patient) utility.getUserFromToken(token);
            if(patient == null) throw new HospitalManagementException("Patient not found.");
            if (patient.getAppointments().stream().noneMatch(appointment -> appointment.getId().equals(appointmentData.getId())))
                throw new HospitalManagementException("You are not authorized to access this appointment.");
            appointmentService.cancelAppointment(
                    appointmentData.getId(),
                    appointmentData.getCancellationReason(),
                    patient.getFullName());
            response.setMessage("Appointment cancelled Successfully");
            response.setStatus(HttpStatus.OK);
        } catch (HospitalManagementException e) {
            response.setError(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.setError("Error while cancelling appointment: "+ e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/patient/appointment/delete")
    public ResponseEntity<Response> removeAppointment(@RequestHeader("Authorization") String token,
                                                      @RequestBody AppointmentData appointmentData) {
        Response response = new Response();
        try{
            Patient patient = (Patient) utility.getUserFromToken(token);
            if(patient == null) throw new HospitalManagementException("Patient not found.");
            Appointment appointment = appointmentRepo.findById(appointmentData.getId()).
                    orElseThrow(() -> new HospitalManagementException("Appointment not found"));
            appointmentService.removeOldCanceledAppointments(patient, appointment);
            response.setMessage("Appointment removed successfully");
            response.setStatus(HttpStatus.OK);
        } catch (HospitalManagementException e) {
            response.setError(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.setError("Error while cancelling appointment: "+ e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
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

    @PutMapping("/doctor/appointment")
    public ResponseEntity<Response> updateAppointmentStatus(@RequestHeader("Authorization") String token,
                                                            @RequestBody AppointmentData appointmentData) {
        Response response = new Response();
        try {
            Doctor doctor = (Doctor) utility.getUserFromToken(token);
            if(doctor == null) throw new HospitalManagementException("Doctor not found.");
            Appointment appointment = appointmentRepo.findById(appointmentData.getId()).orElseThrow(
                    () -> new HospitalManagementException("Appointment not found"));
            if (!appointment.getDoctor().getId().equals(doctor.getId()))
                throw new HospitalManagementException("You are not authorized to access this appointment.");
            appointmentService.UpdateAppointmentStatus(appointment, appointmentData.getStatus());
            response.setMessage("Appointment updated Successfully");
            response.setStatus(HttpStatus.OK);
        } catch (HospitalManagementException e) {
            response.setError(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.setError("Error while updating appointment: "+ e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/doctor/appointment/cancel")
    public ResponseEntity<Response> cancelAppointmentDoctor(@RequestHeader("Authorization") String token,
                                                            @RequestBody AppointmentData appointmentData){
        Response response = new Response();
        try {
            Doctor doctor = (Doctor) utility.getUserFromToken(token);
            if(doctor == null) throw new HospitalManagementException("Doctor not found.");
            if (doctor.getAppointments().stream().noneMatch(appointment -> appointment.getId().equals(appointmentData.getId())))
                throw new HospitalManagementException("You are not authorized to access this appointment.");
            appointmentService.cancelAppointment(
                    appointmentData.getId(),
                    appointmentData.getCancellationReason(),
                    doctor.getFullName()
            );
            response.setMessage("Appointment cancelled Successfully");
            response.setStatus(HttpStatus.OK);
        } catch (HospitalManagementException e) {
            response.setError(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.setError("Error while cancelling appointment: "+ e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
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

    @PutMapping("/admin/appointment/cancel")
    public ResponseEntity<Response> cancelAppointmentAdmin(@RequestHeader("Authorization") String token,
                                                           @RequestBody AppointmentData appointmentData){
        Response response = new Response();
        try {
            Admin admin = (Admin) utility.getUserFromToken(token);
            if(admin == null) throw new HospitalManagementException("Admin not found.");
            appointmentService.cancelAppointment(
                    appointmentData.getId(),
                    appointmentData.getCancellationReason(),
                    admin.getFullName());
            response.setMessage("Appointment cancelled Successfully");
            response.setStatus(HttpStatus.OK);
        } catch (HospitalManagementException e) {
            response.setError(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.setError("Error while cancelling appointment: "+ e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
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
