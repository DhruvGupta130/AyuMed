package com.example.system.repository;

import com.example.system.entity.Doctor;
import com.example.system.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface ScheduleRepo extends JpaRepository<Schedule, Long> {

    Optional<Schedule> findByDoctorId(Long doctorId);
    Optional<Schedule> findByDoctorAndDate(@Param("doctor") Doctor doctor, @Param("date") LocalDate date);

}
