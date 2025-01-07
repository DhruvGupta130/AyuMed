package com.example.system.service;

import com.example.system.dto.DoctorDTO;
import com.example.system.dto.Password;
import com.example.system.dto.ProfileUpdateDTO;
import com.example.system.dto.RegistrationDTO;
import com.example.system.entity.Doctor;
import com.example.system.entity.Hospital;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface DoctorService {

    Doctor getDoctorById(long id);
    void deleteProfile(Doctor doctor);
    List<Doctor> searchDoctors(String specialty, Boolean available, String department);
    void updateDoctor(Doctor doctor, ProfileUpdateDTO updateDTO, MultipartFile file);
    void updatePassword(Doctor doctor, Password password);
    List<DoctorDTO> getDoctorBySearch(String keyword);
    List<DoctorDTO> getHospitalDoctors(Hospital hospital);
    void registerDoctor(RegistrationDTO doctor);
    void saveFromExcel(MultipartFile file, long hospitalId);
}

