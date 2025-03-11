package com.example.system.service;

import com.example.system.entity.Doctor;
import com.example.system.entity.Schedule;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SlotInitializationService {

    void initializeAvailableSlots(Doctor doctor, List<Schedule> schedules);
    void clearAllAvailableSlots(Doctor doctor);
    void clearSelectedSlots(Doctor doctor, List<Schedule> schedules);
}
