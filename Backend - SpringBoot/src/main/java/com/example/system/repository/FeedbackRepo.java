package com.example.system.repository;

import com.example.system.entity.Doctor;
import com.example.system.entity.Feedback;
import com.example.system.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepo extends JpaRepository<Feedback, Long> {
    Feedback findByAppointmentId(Long appointmentId);

    @Query("SELECT f FROM Feedback f JOIN f.appointment a JOIN a.doctor d WHERE d.hospital = :hospital")
    List<Feedback> getFeedbacksByHospital(Hospital hospital);

    @Query("SELECT f FROM Feedback f JOIN f.appointment a WHERE a.doctor = :doctor")
    List<Feedback> getFeedbacksByDoctor(Doctor doctor);
}
