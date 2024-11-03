package com.example.system.repository;

import com.example.system.entity.MedicalTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalTestRepo extends JpaRepository<MedicalTest, Integer> {

}
