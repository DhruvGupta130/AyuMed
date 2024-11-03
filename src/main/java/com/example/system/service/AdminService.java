package com.example.system.service;

import com.example.system.dto.AppointmentDTO;
import com.example.system.dto.DoctorDTO;
import com.example.system.dto.PatientDTO;
import com.example.system.dto.ProfileUpdateDTO;
import com.example.system.entity.Admin;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminService {

    void deleteProfile(Admin admin);
    List<DoctorDTO> getAllDoctors();
    List<PatientDTO> getAllPatients();
    List<AppointmentDTO> getAllAppointments();
    void updateProfile(Admin admin, ProfileUpdateDTO updateDTO);

}
