package com.example.system.dto;

import com.example.system.entity.Appointment;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDate;

public class AppointmentSpecifications {

    public static Specification<Appointment> betweenDates(LocalDate startDate, LocalDate endDate) {
        return (root, _, criteriaBuilder) -> {
            if (startDate == null && endDate == null) {
                return criteriaBuilder.conjunction();
            } else if (startDate != null && endDate != null) {
                return criteriaBuilder.between(root.get("appointmentDate"), startDate, endDate);
            } else if (startDate != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("appointmentDate"), startDate);
            } else {
                return criteriaBuilder.lessThanOrEqualTo(root.get("appointmentDate"), endDate);
            }
        };
    }

    public static Specification<Appointment> hasStatus(AppointmentStatus status) {
        return (root, _, criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    public static Specification<Appointment> byDoctorId(Long doctorId) {
        return (root, _, criteriaBuilder) -> {
            if (doctorId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("doctor").get("id"), doctorId);
        };
    }
}
