package com.example.system.repository;

import com.example.system.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepo extends JpaRepository<Doctor, Long> {

    @Query("SELECT d FROM Doctor d JOIN d.loginUser u WHERE u.username = :username")
    Optional<Doctor> findByUsername(String username);

    Optional<Doctor> getDoctorById(Long id);

    @Query("SELECT d FROM Doctor d LEFT JOIN d.schedules s WHERE " +
            "(:specialty IS NULL OR d.specialty = :specialty) AND " +
            "(:department IS NULL OR d.department = :department) AND " +
            "(:available IS NULL OR EXISTS (SELECT s2 FROM Schedule s2 WHERE s2.doctor = d AND EXISTS (SELECT t from TimeSlot t where t.available = true)))")
    List<Doctor> searchDoctors(String specialty, Boolean available, String department);

    List<Doctor> findAllByDepartment(String department);

    @Query("SELECT DISTINCT d.department FROM Doctor d WHERE d.department IS NOT NULL")
    List<String> findAllDepartments();

    @Query("SELECT COUNT(d) FROM Doctor d WHERE d.department = :department")
    int countByDepartment(String department);

}
