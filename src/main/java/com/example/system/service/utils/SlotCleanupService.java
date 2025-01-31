package com.example.system.service.utils;

import com.example.system.entity.Schedule;
import com.example.system.service.DoctorService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class SlotCleanupService {


    private final DoctorService doctorService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteOldSlots() {

        LocalDate now = LocalDateTime.now().toLocalDate();
        doctorService.getAllDoctors().forEach(doctor -> {
            List<Long> oldSchedules = doctor.getSchedules().stream()
                    .filter(s -> s.getDate().isBefore(now))
                    .map(Schedule::getId)
                    .toList();
            if (!oldSchedules.isEmpty()) {
                doctorService.deleteSelectedSchedules(doctor, oldSchedules);
            }
        });
        System.out.println("Old slots cleaned up successfully at: " + now);
    }
}
