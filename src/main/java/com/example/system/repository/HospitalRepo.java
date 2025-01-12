package com.example.system.repository;

import com.example.system.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalRepo extends JpaRepository<Hospital, Long> {

    @Query("SELECT h FROM Hospital h WHERE " +
            "(6371 * acos(cos(radians(:lat)) * cos(radians(h.address.latitude)) " +
            "* cos(radians(h.address.longitude) - radians(:lng)) + sin(radians(:lat)) " +
            "* sin(radians(h.address.latitude)))) <= :radius")
    List<Hospital> findHospitalsWithinRadius(@Param("lat") double latitude,
                                             @Param("lng") double longitude,
                                             @Param("radius") double radius);

    @Query("SELECT h FROM Hospital h WHERE (h.HospitalName LIKE %:keyword%) OR :keyword IS null")
    List<Hospital> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT h.departments FROM Hospital h WHERE h = :hospital")
    List<String> getAllDepartments(Hospital hospital);
}
