package com.example.system.repository;

import com.example.system.entity.MedicalHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalHistoryRepo extends JpaRepository<MedicalHistory, Long> {
}
