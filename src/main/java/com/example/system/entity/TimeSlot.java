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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalTime startTime;
    private LocalTime endTime;
    private long slotIndex;

    private boolean available = true;

    @JsonIgnore
    @ManyToOne
    private Schedule schedule;

    public TimeSlot(LocalTime startTime, LocalTime endTime, Schedule schedule, long slotIndex) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.schedule = schedule;
        this.slotIndex = slotIndex;
    }

    public void reduceAvailability() {
        this.available = false;
    }

    public void restoreAvailability() {
        this.available = true;
    }
}
