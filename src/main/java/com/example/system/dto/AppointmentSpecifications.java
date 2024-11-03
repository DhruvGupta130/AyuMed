package com.example.system.dto;

import com.example.system.entity.Appointment;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDate;

public class AppointmentSpecifications {

    // Filter by appointment date range
    public static Specification<Appointment> betweenDates(LocalDate startDate, LocalDate endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null && endDate == null) {
                return criteriaBuilder.conjunction(); // No filter if both are null
            } else if (startDate != null && endDate != null) {
                return criteriaBuilder.between(root.get("appointmentDate"), startDate, endDate);
            } else if (startDate != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("appointmentDate"), startDate);
            } else {
                return criteriaBuilder.lessThanOrEqualTo(root.get("appointmentDate"), endDate);
            }
        };
    }

    // Filter by appointment status
    public static Specification<Appointment> hasStatus(AppointmentStatus status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction(); // No filter if status is null
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    // Filter by doctor ID
    public static Specification<Appointment> byDoctorId(Long doctorId) {
        return (root, query, criteriaBuilder) -> {
            if (doctorId == null) {
                return criteriaBuilder.conjunction(); // No filter if doctorId is null
            }
            return criteriaBuilder.equal(root.get("doctor").get("id"), doctorId);
        };
    }
}
