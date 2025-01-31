package com.example.system.repository;

import com.example.system.entity.Doctor;
import com.example.system.entity.Schedule;
import com.example.system.entity.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepo extends JpaRepository<Schedule, Long> {

    Optional<Schedule> findByDoctorAndDate(@Param("doctor") Doctor doctor, @Param("date") LocalDate date);

    List<Schedule> findScheduleByDoctor(Doctor doctor);

    @Query("SELECT t FROM TimeSlot t JOIN t.schedule s " +
            "WHERE s.date = :date AND s.doctor = :doctor " +
            "AND (s.date > CURRENT_DATE OR (s.date = CURRENT_DATE AND t.startTime > CURRENT_TIME))")
    List<TimeSlot> findAllByDateAndAvailableAndDoctor(@Param("date") LocalDate date, @Param("doctor") Doctor doctor);



    boolean existsByDoctorAndDate(Doctor doctor, LocalDate date);
}
