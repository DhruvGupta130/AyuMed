package com.example.system.service.Impl;

import com.example.system.dto.AppointmentStatus;
import com.example.system.dto.DoctorDTO;
import com.example.system.dto.HospitalDTO;
import com.example.system.dto.Search;
import com.example.system.entity.Appointment;
import com.example.system.entity.Doctor;
import com.example.system.entity.Patient;
import com.example.system.repository.AppointmentRepo;
import com.example.system.repository.DoctorRepo;
import com.example.system.repository.PatientRepo;
import com.example.system.service.AppointmentService;
import com.example.system.service.DashboardService;
import com.example.system.service.DoctorService;
import com.example.system.service.HospitalService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final DoctorRepo doctorRepo;
    private final PatientRepo patientRepo;
    private final AppointmentRepo appointmentRepo;
    private final AppointmentService appointmentService;
    private final HospitalService hospitalService;
    private final DoctorService doctorService;

    @Override
    public Map<String, Object> getGeneralStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalDoctors", doctorRepo.count());
        stats.put("totalPatients", patientRepo.count());
        stats.put("totalAppointments", appointmentRepo.count());
        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        LocalDateTime endOfToday = LocalDate.now().atTime(23, 59, 59);
        stats.put("todayAppointments", appointmentRepo.countByAppointmentDateBetween(startOfToday, endOfToday));
        return stats;
    }

    @Override
    public Map<String, Map<String, Long>> getAppointmentAnalysis() {
        Map<String, Map<String, Long>> analysis = new HashMap<>();
        Map<String, Long> statusCounts = appointmentRepo.findAll().stream()
                .collect(Collectors.groupingBy(appointment -> appointment.getStatus().name(), Collectors.counting()));
        analysis.put("statusCounts", statusCounts);
        Map<String, Long> departmentCounts = appointmentRepo.findAll().stream()
                .collect(Collectors.groupingBy(appointment -> appointment.getDoctor().getDepartment(), Collectors.counting()));
        analysis.put("departmentCounts", departmentCounts);
        return analysis;
    }

    @Override
    public Map<String, Map<String, Long>> getDemographics() {
        Map<String, Map<String, Long>> demographics = new HashMap<>();
        Map<String, Long> genderCounts = patientRepo.findAll().stream()
                .collect(Collectors.groupingBy(patient -> patient.getGender().name(), Collectors.counting()));
        demographics.put("patientGenderDistribution", genderCounts);
        Map<String, Long> departmentCounts = doctorRepo.findAll().stream()
                .collect(Collectors.groupingBy(Doctor::getDepartment, Collectors.counting()));
        demographics.put("doctorDepartmentDistribution", departmentCounts);

        Map<String, Long> ageGroupCounts = calculateAgeGroupDistribution();
        demographics.put("patientAgeGroupDistribution", ageGroupCounts);
        return demographics;
    }

    private Map<String, Long> calculateAgeGroupDistribution() {
        Map<String, Long> ageGroups = new HashMap<>();
        LocalDate now = LocalDate.now();
        for (Patient patient : patientRepo.findAll()) {
            int age = Period.between(patient.getDateOfBirth(), now).getYears();
            String ageGroup = (age <= 18) ? "0-18" : (age <= 35) ? "19-35" : (age <= 50) ? "36-50" : "51+";
            ageGroups.put(ageGroup, ageGroups.getOrDefault(ageGroup, 0L) + 1);
        }
        return ageGroups;
    }

    @Override
    public Map<String, Object> getPerformanceMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        Map<String, Double> averageDurationByDoctor = appointmentRepo.findAll().stream()
                .collect(Collectors.groupingBy(a -> a.getDoctor().getFullName(),
                        Collectors.averagingDouble(a -> Duration.between(a.getCreatedAt(), a.getUpdatedAt()).toMinutes())));
        metrics.put("averageDurationByDoctor", averageDurationByDoctor);
        Map<YearMonth, Long> patientGrowth = patientRepo.findAll().stream()
                .collect(Collectors.groupingBy(p -> YearMonth.from(p.getCreatedAt().toLocalDate()), Collectors.counting()));
        metrics.put("patientGrowth", patientGrowth);
        return metrics;
    }

    @Override
    public Map<String, Object> getTrendData() {
        Map<String, Object> trendData = new HashMap<>();
        Map<YearMonth, Long> monthlyAppointments = appointmentRepo.findAll().stream()
                .collect(Collectors.groupingBy(a -> YearMonth.from(a.getAppointmentDate()), Collectors.counting()));
        trendData.put("monthlyAppointments", monthlyAppointments);
        Map<DayOfWeek, Map<AppointmentStatus, Long>> weeklyTrends = appointmentRepo.findAll().stream()
                .collect(Collectors.groupingBy(a -> a.getAppointmentDate().getDayOfWeek(),
                        Collectors.groupingBy(Appointment::getStatus, Collectors.counting())));
        trendData.put("weeklyTrends", weeklyTrends);
        return trendData;
    }

    @Override
    public Map<String, Integer> getAllDepartments() {
        return doctorRepo.findAllDepartments().stream()
                .collect(Collectors.toMap(department -> department, doctorRepo::countByDepartment));
    }

    @Override
    public List<Appointment> filterAppointments(LocalDate startDate, LocalDate endDate, AppointmentStatus status, Long doctorId) {
        return appointmentService.filterAppointments(startDate, endDate, status, doctorId);
    }

    @Override
    public Search searchByKeyword(String keyword) {
        List<HospitalDTO> hospitals = hospitalService.searchHospital(keyword);
        List<DoctorDTO> doctors = doctorService.getDoctorBySearch(keyword);
        return new Search(hospitals, doctors);
    }

}
