package com.example.system.service;

import com.example.system.dto.AppointmentStatus;
import com.example.system.dto.Search;
import com.example.system.entity.Appointment;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public interface DashboardService {

    Map<String, Object> getGeneralStatistics();
    Map<String, Map<String, Long>> getAppointmentAnalysis();
    Map<String, Map<String, Long>> getDemographics();
    Map<String, Object> getPerformanceMetrics();
    Map<String, Object> getTrendData();
    Map<String, Integer> getAllDepartments();
    List<Appointment> filterAppointments(LocalDate startDate, LocalDate endDate, AppointmentStatus status, Long doctorId);
    Search searchByKeyword(String keyword);


}
