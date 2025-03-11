package com.example.system.service.impl;

import com.example.system.entity.Doctor;
import com.example.system.entity.Schedule;
import com.example.system.repository.ScheduleRepo;
import com.example.system.service.SlotInitializationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class SlotInitializationServiceImpl implements SlotInitializationService {

    private final ScheduleRepo scheduleRepo;

    @Override
    @Transactional
    public void initializeAvailableSlots(Doctor doctor, List<Schedule> schedules) {
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusMonths(1);
        schedules.forEach(schedule -> {
            DayOfWeek scheduledDay = schedule.getDayOfWeek();
            LocalTime startTime = schedule.getStartTime();
            LocalTime endTime = schedule.getEndTime();
            Stream.iterate(today, date -> date.plusDays(1))
                    .limit(ChronoUnit.DAYS.between(today, endDate) + 1)
                    .filter(date -> scheduledDay.equals(date.getDayOfWeek()))
                    .filter(date -> !scheduleRepo.existsByDoctorAndDate(doctor, date))
                    .forEach(date -> {
                        Schedule newSchedule = new Schedule();
                        newSchedule.setDoctor(doctor);
                        newSchedule.setDate(date);
                        newSchedule.setDayOfWeek(scheduledDay);
                        newSchedule.setStartTime(startTime);
                        newSchedule.setEndTime(endTime);
                        newSchedule.initializeAvailableSlots();
                        scheduleRepo.save(newSchedule);
                    });
        });
    }

    @Override
    @Transactional
    public void clearAllAvailableSlots(Doctor doctor) {
        List<Schedule> schedules = new ArrayList<>(doctor.getSchedules());
        doctor.getSchedules().clear();
        scheduleRepo.deleteAll(schedules);
    }

    @Override
    @Transactional
    public void clearSelectedSlots(Doctor doctor, List<Schedule> schedules) {
        doctor.getSchedules().removeAll(schedules);
        scheduleRepo.deleteAll(schedules);
    }
}
