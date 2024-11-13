package com.example.system.repository;

import com.example.system.entity.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PharmacyRepo extends JpaRepository<Pharmacy, Long> {
}
