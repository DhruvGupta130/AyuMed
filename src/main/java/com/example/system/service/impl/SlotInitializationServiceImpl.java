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
import java.util.List;

@Service
@AllArgsConstructor
public class SlotInitializationServiceImpl implements SlotInitializationService {

    private final ScheduleRepo scheduleRepo;

    @Override
    @Transactional
    public void initializeAvailableSlots(Doctor doctor, List<Schedule> schedules) {
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusMonths(1);

        for (Schedule scheduleTemplate : schedules) {
            DayOfWeek scheduledDay = scheduleTemplate.getDayOfWeek();
            LocalTime startTime = scheduleTemplate.getStartTime();
            LocalTime endTime = scheduleTemplate.getEndTime();

            for (LocalDate date = today; !date.isAfter(endDate); date = date.plusDays(1)) {
                if (scheduledDay.equals(date.getDayOfWeek())) {
                    Schedule newSchedule = new Schedule();
                    newSchedule.setDoctor(doctor);
                    newSchedule.setDate(date);
                    newSchedule.setDayOfWeek(scheduledDay);
                    newSchedule.setStartTime(startTime);
                    newSchedule.setEndTime(endTime);
                    newSchedule.initializeAvailableSlots();
                    scheduleRepo.save(newSchedule);
                }
            }
        }
    }
}
