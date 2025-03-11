package com.example.system.service;

import com.example.system.dto.*;
import com.example.system.entity.Doctor;
import com.example.system.entity.Hospital;
import com.example.system.entity.Schedule;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface DoctorService {

    Doctor getDoctorById(long id);
    DoctorDTO getDoctorDTOById(long id);
    DoctorDTO getDoctorProfile(Doctor doctor);
    List<DoctorDTO> searchDoctors(String specialty, Boolean available, String department);
    void updateDoctor(Doctor doctor, DoctorUpdateDTO updateDTO);
    void addSchedule(Doctor doctor, List<Schedule> schedules);
    void updateSchedule(Doctor doctor, List<Schedule> schedule);
    void updatePassword(Doctor doctor, Password password);
    void updatePassword(Doctor doctor, String password);
    List<DoctorDTO> getDoctorsBySearch(String keyword);
    List<DoctorDTO> getDoctorsByDepartment(String department);
    List<DoctorDTO> getDoctorsByHospitalAndDepartment(Hospital hospital, String department);
    List<DoctorDTO> getDoctorsByHospital(Hospital hospital);
    List<DoctorDTO> getHospitalDoctors(Hospital hospital);
    List<ScheduleDTO> getSchedules(Doctor doctor, String sortBy, String sortDirection);
    void deleteAllSchedules(Doctor doctor);
    void deleteSelectedSchedules(Doctor doctor, List<Long> scheduleIds);
    void registerDoctor(RegistrationDTO doctor);
    void saveFromExcel(MultipartFile file, long hospitalId);
    List<Doctor> getAllDoctors();
    List<DoctorDTO> getAllTheDoctors();
}

