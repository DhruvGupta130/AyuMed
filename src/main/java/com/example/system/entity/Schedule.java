package com.example.system.entity;

import com.example.system.exception.HospitalManagementException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalTime startTime;
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;
    private LocalDate date;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TimeSlot> timeSlots = new ArrayList<>();

    @JsonIgnore
    @ManyToOne
    private Doctor doctor;

    @Transient
    private final Duration averageConsultationTime = Duration.ofMinutes(15);


    public void initializeAvailableSlots() {
        LocalTime startTime = this.startTime;
        long slotIndex = 0L;
        while (startTime.isBefore(endTime)) {
            LocalTime slotEndTime = startTime.plus(averageConsultationTime);
            if (slotEndTime.isAfter(endTime)) break;
            slotIndex++;
            timeSlots.add(new TimeSlot(startTime, slotEndTime, this, slotIndex));
            startTime = slotEndTime;
        }
    }

    public void bookSlot(int slotIndex) {
        TimeSlot slot = findTimeSlotByStartTimeAndSlot(slotIndex);
        if (slot != null && slot.isAvailable()) {
            slot.reduceAvailability();
        } else {
            throw new HospitalManagementException("Time slot not available or doesn't exist.");
        }
    }

    public void restoreSlot(int slotIndex) {
        TimeSlot slot = findTimeSlotByStartTimeAndSlot(slotIndex);
        if (slot != null) {
            slot.restoreAvailability();
        }
    }

    public LocalTime getAppointmentTime(int slotIndex) {
        if (slotIndex < 0 || slotIndex >= timeSlots.size()) {
            throw new HospitalManagementException("Slot index is out of bounds.");
        }
        return timeSlots.get(slotIndex).getStartTime();
    }

    public boolean hasAvailableSlots() {
        return timeSlots.stream().anyMatch(TimeSlot::isAvailable);
    }

    public int getAvailableSlotCount() {
        return (int) timeSlots.stream().filter(TimeSlot::isAvailable).count();
    }

    public TimeSlot findTimeSlotByStartTimeAndSlot(long slotIndex) {
        return timeSlots.stream()
                .filter(slot -> slot.getSlotIndex() == slotIndex).findFirst()
                .orElseThrow(() -> new HospitalManagementException("Time slot not found."));
    }

}
