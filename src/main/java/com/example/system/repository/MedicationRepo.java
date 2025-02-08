package com.example.system.repository;

import com.example.system.entity.Medication;
import com.example.system.entity.Patient;
import com.example.system.entity.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicationRepo extends JpaRepository<Medication, Long> {

    @Query("SELECT m FROM Medication m WHERE m.patients IS EMPTY " +
            "AND m.batchNumber = :batchNumber " +
            "AND m.medicationName = :name " +
            "AND m.expiry = :expiry")
    Optional<Medication> findExistingMedication(@Param("batchNumber") String batchNumber,
                                                @Param("name") String medicationName,
                                                @Param("expiry") LocalDate expiry);

    @Query("SELECT m FROM Medication m WHERE (:keyword IS NULL " +
            "OR m.medicationName LIKE %:keyword% OR m.compositionName LIKE %:keyword%)")
    List<Medication> findByKeyword(String keyword);

    @Query("SELECT p FROM Pharmacy p WHERE " +
            "(6371 * acos(cos(radians(:lat)) * cos(radians(p.address.latitude)) " +
            "* cos(radians(p.address.longitude) - radians(:lng)) + sin(radians(:lat)) " +
            "* sin(radians(p.address.latitude)))) <= :radius")
    List<Pharmacy> findPharmacyWithinRadius(@Param("lat") double latitude,
                                             @Param("lng") double longitude,
                                             @Param("radius") double radius);

    @Query("SELECT m FROM Medication m JOIN m.patients p WHERE p = :patient")
    List<Medication> getMedicationsByPatient(Patient patient);

}