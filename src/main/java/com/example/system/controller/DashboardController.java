package com.example.system.controller;

import com.example.system.dto.AppointmentStatus;
import com.example.system.entity.Appointment;
import com.example.system.service.AppointmentService;
import com.example.system.service.DashboardService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getGeneralStatistics() {
        return ResponseEntity.ok(dashboardService.getGeneralStatistics());
    }

    @GetMapping("/appointments/analysis")
    public ResponseEntity<Map<String, Map<String, Long>>> getAppointmentAnalysis() {
        return ResponseEntity.ok(dashboardService.getAppointmentAnalysis());
    }

    @GetMapping("/demographics")
    public ResponseEntity<Map<String, Map<String, Long>>> getDemographics() {
        return ResponseEntity.ok(dashboardService.getDemographics());
    }

    @GetMapping("/performance")
    public ResponseEntity<Map<String, Object>> getPerformanceMetrics() {
        return ResponseEntity.ok(dashboardService.getPerformanceMetrics());
    }

    @GetMapping("/trends")
    public ResponseEntity<Map<String, Object>> getTrendData() {
        return ResponseEntity.ok(dashboardService.getTrendData());
    }

    @GetMapping("/departments")
    public ResponseEntity<Map<String, Integer>> getDepartments() {
        return ResponseEntity.ok(dashboardService.getAllDepartments());
    }

    @GetMapping("/api/patient/appointments/filter")
    public ResponseEntity<List<Appointment>> filterAppointments(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) AppointmentStatus status,
            @RequestParam(required = false) Long doctorId) {
        return ResponseEntity.ok(dashboardService.filterAppointments(startDate, endDate, status, doctorId));
    }

}
