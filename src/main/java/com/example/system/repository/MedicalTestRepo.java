package com.example.system.repository;

import com.example.system.entity.MedicalTest;
import com.example.system.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalTestRepo extends JpaRepository<MedicalTest, Integer> {

    @Query("SELECT m FROM MedicalTest m WHERE m.history.patient = :patient")
    List<MedicalTest> findMedicalTestByPatient(Patient patient);
}
