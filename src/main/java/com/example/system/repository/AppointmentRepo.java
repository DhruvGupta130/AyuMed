package com.example.system.repository;

import com.example.system.entity.Appointment;
import com.example.system.entity.Doctor;
import com.example.system.entity.Patient;
import jakarta.validation.constraints.FutureOrPresent;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepo extends JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {

    List<Appointment> findAllByDoctor(Doctor doctor, Sort sort);
    List<Appointment> findAllByPatient(Patient patient, Sort sort);

    @Query("select count(a) from Appointment a where a.doctor = :doctor and date(a.appointmentDate) = :date")
    long countByDoctorAndAppointmentDate(Doctor doctor, LocalDate date);

    List<Appointment> findByAppointmentDateBetween(
            @FutureOrPresent(message = "Appointment date must not be in the past") LocalDateTime start,
            @FutureOrPresent(message = "Appointment date must not be in the past") LocalDateTime end
    );

    @Query("select a from Appointment a where a.appointmentDate between :start and :end")
    List<Appointment> findAppointmentsByDate(LocalDateTime start, LocalDateTime end);

    long countByAppointmentDateBetween(LocalDateTime start, LocalDateTime end);

    @Modifying
    @Query("UPDATE Appointment a SET a.status = 'EXPIRED' WHERE a.appointmentDate < :now AND a.status NOT IN ('COMPLETED', 'CANCELLED')")
    void markExpiredAppointments(LocalDateTime now);

}
