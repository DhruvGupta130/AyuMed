package com.example.system.controller;

import com.example.system.dto.AppointmentDTO;
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
        System.out.println(appointmentData);
        Response response = new Response();
        try {
            Object user = utility.getUserFromToken(token);
            if(!(user instanceof Patient patient))
                throw new HospitalManagementException("Unauthorized: User is not a patient.");
            Doctor doctor = doctorRepo.getDoctorById(appointmentData.getDoctorId()).orElseThrow(
                    () -> new HospitalManagementException("Doctor not found"));
            appointmentService.scheduleAppointment(patient, doctor, appointmentData.getAppointmentDate(),
                    patient.getFirstName() + " " + patient.getLastName(), appointmentData.getSlot());
            response.setMessage("Appointment scheduled Successfully");
            response.setStatus(HttpStatus.OK);
        } catch (HospitalManagementException e) {
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.setMessage("Error while scheduling appointment: "+ e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/patient/appointment/cancel")
    public ResponseEntity<Response> cancelAppointmentPatient(@RequestHeader("Authorization") String token,
                                                             @RequestBody AppointmentData appointmentData){
        Response response = new Response();
        try {
            Object user = utility.getUserFromToken(token);
            if(!(user instanceof Patient patient))
                throw new HospitalManagementException("Unauthorized: User is not a patient.");
            if (patient.getAppointments().stream().noneMatch(appointment -> appointment.getId().equals(appointmentData.getId())))
                throw new HospitalManagementException("You are not authorized to access this appointment.");
            appointmentService.cancelAppointment(
                    appointmentData.getId(),
                    appointmentData.getCancellationReason(),
                    patient.getFullName());
            response.setMessage("Appointment cancelled Successfully");
            response.setStatus(HttpStatus.OK);
        } catch (HospitalManagementException e) {
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.setMessage("Error while cancelling appointment: "+ e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/patient/appointment/delete")
    public ResponseEntity<Response> removeAppointment(@RequestHeader("Authorization") String token,
                                                      @RequestBody AppointmentData appointmentData) {
        Response response = new Response();
        try{
            Object user = utility.getUserFromToken(token);
            if(!(user instanceof Patient patient))
                throw new HospitalManagementException("Unauthorized: User is not a patient.");
            Appointment appointment = appointmentRepo.findById(appointmentData.getId()).
                    orElseThrow(() -> new HospitalManagementException("Appointment not found"));
            appointmentService.removeOldCanceledAppointments(patient, appointment);
            response.setMessage("Appointment removed successfully");
            response.setStatus(HttpStatus.OK);
        } catch (HospitalManagementException e) {
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.setMessage("Error while cancelling appointment: "+ e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/patient/appointments")
    public ResponseEntity<List<AppointmentDTO>> getPatientAppointments(@RequestHeader("Authorization") String token) {
        Object user = utility.getUserFromToken(token);
        if (!(user instanceof Patient patient))
            throw new HospitalManagementException("Unauthorized access: User is not a patient.");
        List<Appointment> appointments = appointmentRepo.findAllByPatient(patient);
        List<AppointmentDTO> appointmentDTOs = appointments.stream()
                .map(appointmentService::getAppointment)
                .toList();
        return ResponseEntity.ok(appointmentDTOs);
    }

    @PutMapping("/doctor/appointment")
    public ResponseEntity<Response> updateAppointmentStatus(@RequestHeader("Authorization") String token,
                                                            @RequestBody AppointmentData appointmentData) {
        Response response = new Response();
        try {
            Object user = utility.getUserFromToken(token);
            if (!(user instanceof Doctor doctor))
                throw new HospitalManagementException("Unauthorized access: User is not a doctor.");
            Appointment appointment = appointmentRepo.findById(appointmentData.getId()).orElseThrow(
                    () -> new HospitalManagementException("Appointment not found"));
            if (!appointment.getDoctor().getId().equals(doctor.getId()))
                throw new HospitalManagementException("You are not authorized to access this appointment.");
            appointmentService.UpdateAppointmentStatus(appointment, appointmentData.getStatus());
            response.setMessage("Appointment updated Successfully");
            response.setStatus(HttpStatus.OK);
        } catch (HospitalManagementException e) {
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.setMessage("Error while updating appointment: "+ e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/doctor/appointment/cancel")
    public ResponseEntity<Response> cancelAppointmentDoctor(@RequestHeader("Authorization") String token,
                                                            @RequestBody AppointmentData appointmentData){
        Response response = new Response();
        try {
            Object user = utility.getUserFromToken(token);
            if (!(user instanceof Doctor doctor))
                throw new HospitalManagementException("Unauthorized access: User is not a doctor.");
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
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.setMessage("Error while cancelling appointment: "+ e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/doctor/appointments")
    public ResponseEntity<List<AppointmentDTO>> getDoctorAppointments(@RequestHeader("Authorization") String token) {
        Object user = utility.getUserFromToken(token);
        if (!(user instanceof Doctor doctor))
            throw new HospitalManagementException("Unauthorized access: User is not a doctor.");
        List<Appointment> appointments = appointmentRepo.findAllByDoctor(doctor);
        List<AppointmentDTO> appointmentDTOs = appointments.stream()
                .map(appointmentService::getAppointment)
                .toList();
        return ResponseEntity.ok(appointmentDTOs);
    }

    @PutMapping("/admin/appointment/cancel")
    public ResponseEntity<Response> cancelAppointmentAdmin(@RequestHeader("Authorization") String token,
                                                           @RequestBody AppointmentData appointmentData){
        Response response = new Response();
        try {
            Object user = utility.getUserFromToken(token);
            if (!(user instanceof Admin admin))
                throw new HospitalManagementException("Unauthorized access: User is not an admin.");
            appointmentService.cancelAppointment(
                    appointmentData.getId(),
                    appointmentData.getCancellationReason(),
                    admin.getFullName());
            response.setMessage("Appointment cancelled Successfully");
            response.setStatus(HttpStatus.OK);
        } catch (HospitalManagementException e) {
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.setMessage("Error while cancelling appointment: "+ e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/admin/appointments/filter")
    public ResponseEntity<List<AppointmentDTO>> filterAppointments(
            @RequestHeader("Authorization") String token,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) AppointmentStatus status,
            @RequestParam(required = false) Long doctorId) {
        Object user = utility.getUserFromToken(token);
        if (!(user instanceof Admin))
            throw new HospitalManagementException("Unauthorized access: User is not an admin.");
        List<Appointment> appointments = appointmentService.filterAppointments(startDate, endDate, status, doctorId);
        return ResponseEntity.ok(appointments.stream().map(appointmentService::getAppointment).toList());
    }

    @GetMapping("/admin/appointments")
    public ResponseEntity<List<AppointmentDTO>> getAppointments(@RequestHeader("Authorization") String token) {
        Object user = utility.getUserFromToken(token);
        if (!(user instanceof Admin))
            throw new HospitalManagementException("Unauthorized access: User is not an admin.");
        List<AppointmentDTO> appointmentDTOs = appointmentService.getAllAppointments().stream()
                .map(appointmentService::getAppointment)
                .toList();
        return ResponseEntity.ok(appointmentDTOs);
    }

}
