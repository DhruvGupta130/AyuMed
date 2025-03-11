package com.example.system.repository;

import com.example.system.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepo extends JpaRepository<File, Long> {
    Optional<File> findByFileName(String fileName);
}
