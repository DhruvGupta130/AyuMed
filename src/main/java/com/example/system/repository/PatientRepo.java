package com.example.system.repository;

import com.example.system.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepo extends JpaRepository<Patient, Long> {

    @Query("select p from Patient p join p.loginUser u where u.username = :username")
    Optional<Patient> findByUsername(String username);

    Optional<Patient> getPatientById(Long id);
}

