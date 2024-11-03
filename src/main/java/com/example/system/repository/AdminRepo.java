package com.example.system.repository;

import com.example.system.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepo extends JpaRepository<Admin, Long> {

    @Query("select a from Admin a join a.loginUser u where u.username = :username")
    Optional<Admin> findByUsername(String username);

}
