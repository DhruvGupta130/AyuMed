package com.example.system.service;

import com.example.system.dto.*;
import com.example.system.entity.Doctor;
import com.example.system.entity.Hospital;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface DoctorService {

    Doctor getDoctorById(long id);
    void deleteProfile(Doctor doctor);
    DoctorDTO getDoctorProfile(Doctor doctor);
    List<DoctorDTO> searchDoctors(String specialty, Boolean available, String department);
    void updateDoctor(Doctor doctor, ProfileUpdateDTO updateDTO, MultipartFile file);
    void updatePassword(Doctor doctor, Password password);
    List<DoctorDTO> getDoctorsBySearch(String keyword);
    List<DoctorDTO> getDoctorsByDepartment(String department);
    List<DoctorDTO> getHospitalDoctors(Hospital hospital);
    void registerDoctor(RegistrationDTO doctor);
    void saveFromExcel(MultipartFile file, long hospitalId);
}

