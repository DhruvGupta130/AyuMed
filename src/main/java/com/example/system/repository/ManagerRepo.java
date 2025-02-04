package com.example.system.repository;

import com.example.system.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagerRepo extends JpaRepository<Manager, Long> {

    @Query("select m from Manager m join m.loginUser u where u.username = :username")
    Optional<Manager> findByUsername(String username);

    @Query("select m from Manager m join m.loginUser u where u.email = :email")
    Optional<Manager> findByEmail(String email);
}
