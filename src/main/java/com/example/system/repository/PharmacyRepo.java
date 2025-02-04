package com.example.system.repository;

import com.example.system.entity.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PharmacyRepo extends JpaRepository<Pharmacy, Long> {

    @Query("SELECT p FROM Pharmacy p WHERE p.pharmacyName LIKE %:keyword% OR :keyword IS null")
    List<Pharmacy> findPharmaciesByKeyword(String keyword);

    Pharmacy getPharmacyById(Long id);
}
