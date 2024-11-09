package com.example.system.service;

import com.example.system.dto.DoctorDTO;
import com.example.system.dto.ProfileUpdateDTO;
import com.example.system.entity.Doctor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DoctorService {

    Doctor getDoctorById(long id);
    void deleteProfile(Doctor doctor);
    List<Doctor> searchDoctors(String specialty, Boolean available, String department);
    void updateDoctor(Doctor doctor, ProfileUpdateDTO updateDTO);
    List<DoctorDTO> getDoctorBySearch(String keyword);
}

