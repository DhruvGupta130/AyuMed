package com.example.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private LocalTime startTime;
    private LocalTime endTime;
    private Long slotIndex;

    private boolean available = true;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    public TimeSlot(LocalTime startTime, LocalTime endTime, Schedule schedule, long slotIndex) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.schedule = schedule;
        this.slotIndex = slotIndex;
    }

    public void reduceAvailability() {
        this.available = false; // Slot is no longer available
    }

    public void restoreAvailability() {
        this.available = true; // Restore the availability of the slot
    }
}
