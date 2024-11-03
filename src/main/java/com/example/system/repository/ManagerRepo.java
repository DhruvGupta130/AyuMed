package com.example.system.repository;

import com.example.system.entity.HospitalManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagerRepo extends JpaRepository<HospitalManager, Long> {

    @Query("select m from HospitalManager m join m.loginUser u where u.username = :username")
    Optional<HospitalManager> findByUsername(String username);
}
