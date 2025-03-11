package com.example.system.service;

import com.example.system.dto.Search;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface DashboardService {

    Map<String, Object> getGeneralStatistics();
    Map<String, Map<String, Long>> getAppointmentAnalysis();
    Map<String, Map<String, Long>> getDemographics();
    Map<String, Object> getPerformanceMetrics();
    Map<String, Object> getTrendData();
    Map<String, Integer> getAllDepartments();
    Search searchByKeyword(String keyword);


}
