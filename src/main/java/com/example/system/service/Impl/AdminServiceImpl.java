package com.example.system.service.Impl;

import com.example.system.dto.AppointmentDTO;
import com.example.system.dto.DoctorDTO;
import com.example.system.dto.PatientDTO;
import com.example.system.dto.ProfileUpdateDTO;
import com.example.system.entity.*;
import com.example.system.repository.*;
import com.example.system.service.AdminService;
import com.example.system.service.utils.Utility;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepo userRepo;
    private final AdminRepo adminRepo;
    private final DoctorRepo doctorRepo;
    private final PatientRepo patientRepo;
    private final AppointmentRepo appointmentRepo;

    @Override
    public void deleteProfile(Admin admin) {
        LoginUser user = admin.getLoginUser();
        if(user != null) {
            admin.setLoginUser(null);
            userRepo.delete(user);
        }
        adminRepo.delete(admin);
    }

    @Override
    public List<DoctorDTO> getAllDoctors() {
        List<Doctor> doctors = doctorRepo.findAll();
        return doctors.stream()
                .map(doctor -> new DoctorDTO(
                    doctor.getFirstName(), doctor.getLastName(),
                    doctor.getSpecialty(), doctor.getDepartment(),
                    doctor.getExperience(), doctor.getImage(),
                    doctor.getDegree())
                ).toList();
    }

    @Override
    public List<PatientDTO> getAllPatients() {
        List<Patient> patients = patientRepo.findAll();
        return patients.stream()
                .map(patient -> new PatientDTO(
                        patient.getFirstName(), patient.getLastName(),
                        patient.getDateOfBirth(), patient.getGender(),
                        patient.getNationality(), patient.getImage())
                ).toList();
    }

    @Override
    public List<AppointmentDTO> getAllAppointments() {
        List<Appointment> appointments = appointmentRepo.findAll();
        return appointments.stream()
                .map(appointment -> new AppointmentDTO(
                        Utility.getPatientDTO(appointment.getPatient()),
                        Utility.getDoctorDTO(appointment.getDoctor()),
                        appointment.getAppointmentDate(), appointment.getStatus())
                ).toList();
    }

    @Override
    public void updateProfile(Admin admin, ProfileUpdateDTO updateDTO) {
        admin.setImage(updateDTO.getImage());
        adminRepo.save(admin);
    }
}
