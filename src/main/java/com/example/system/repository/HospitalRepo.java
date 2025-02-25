package com.example.system.repository;

import com.example.system.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HospitalRepo extends JpaRepository<Hospital, Long> {

    @Query("SELECT h FROM Hospital h WHERE " +
            "(6371 * acos(cos(radians(:lat)) * cos(radians(h.address.latitude)) " +
            "* cos(radians(h.address.longitude) - radians(:lng)) + sin(radians(:lat)) " +
            "* sin(radians(h.address.latitude)))) <= :radius")
    List<Hospital> findHospitalsWithinRadius(@Param("lat") double latitude,
                                             @Param("lng") double longitude,
                                             @Param("radius") double radius);

    @Query("SELECT h FROM Hospital h WHERE " +
            "(:keyword IS NULL OR :keyword = '' " +
            "OR LOWER(h.hospitalName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Hospital> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT h FROM Hospital h WHERE :department MEMBER OF h.departments")
    List<Hospital> findHospitalByDepartment(String department);

    @Query("SELECT DISTINCT h.departments FROM Hospital h")
    List<String> getAllDepartments();

    Optional<Hospital> findHospitalById(long id);
}
