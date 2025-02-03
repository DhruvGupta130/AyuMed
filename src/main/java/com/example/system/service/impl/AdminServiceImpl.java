package com.example.system.service.impl;

import com.example.system.dto.AdminDTO;
import com.example.system.dto.AppointmentDTO;
import com.example.system.dto.DoctorDTO;
import com.example.system.dto.PatientDTO;
import com.example.system.entity.*;
import com.example.system.entity.Doctor;
import com.example.system.entity.Patient;
import com.example.system.repository.*;
import com.example.system.service.AdminService;
import com.example.system.service.AppointmentService;
import com.example.system.service.DoctorService;
import com.example.system.service.PatientService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final DoctorRepo doctorRepo;
    private final PatientRepo patientRepo;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final AppointmentService appointmentService;

    @Override
    public AdminDTO getProfile(Admin admin) {
        return new AdminDTO(
                admin.getId(), admin.getFirstName(),
                admin.getLastName(), admin.getGender(),
                admin.getEmail(), admin.getMobile(),
                admin.getFullName()
        );
    }

    @Override
    public List<DoctorDTO> getAllDoctors() {
        List<Doctor> doctors = doctorRepo.findAll();
        return doctors.stream()
                .map(doctorService::getDoctorProfile).toList();
    }

    @Override
    public List<PatientDTO> getAllPatients() {
        List<Patient> patients = patientRepo.findAll();
        return patients.stream()
                .map(patientService::getPatientProfile).toList();
    }

    @Override
    public List<AppointmentDTO> getAllAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        return appointments.stream()
                .map(appointmentService::getAppointment).toList();
    }
}
