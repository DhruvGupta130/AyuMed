package com.example.system.repository;

import com.example.system.entity.LoginUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<LoginUser, Long> {
    Optional<LoginUser> findByUsername(String username);
    Optional<LoginUser> findByEmail(String email);
}
