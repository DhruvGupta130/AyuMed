package com.example.system.repository;

import com.example.system.entity.Pharmacist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PharmacistRepo extends JpaRepository<Pharmacist, Long> {

    @Query("SELECT p FROM Pharmacist p JOIN p.loginUser u WHERE u.username = :username")
    Optional<Pharmacist> findByUsername(String username);

    @Query("select p from Pharmacist p join p.loginUser u where u.email = :email")
    Optional<Pharmacist> findByEmail(String email);
}
