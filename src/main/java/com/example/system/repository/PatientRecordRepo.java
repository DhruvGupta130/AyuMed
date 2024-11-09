package com.example.system.repository;

import com.example.system.entity.Patient;
import com.example.system.entity.PatientRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientRecordRepo extends JpaRepository<PatientRecord, Long> {
    List<PatientRecord> findByPatient(Patient patient);
}
