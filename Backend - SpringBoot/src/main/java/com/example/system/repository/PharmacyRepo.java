package com.example.system.repository;

import com.example.system.entity.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PharmacyRepo extends JpaRepository<Pharmacy, Long> {

    @Query("SELECT p FROM Pharmacy p WHERE " +
            "(:keyword IS NULL OR :keyword = '' " +
            "OR LOWER(p.pharmacyName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Pharmacy> findPharmaciesByKeyword(@Param("keyword") String keyword);

    Pharmacy getPharmacyById(Long id);
}
