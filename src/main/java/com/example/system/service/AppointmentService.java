package com.example.system.service;

import com.example.system.dto.AppointmentDTO;
import com.example.system.dto.AppointmentStatus;
import com.example.system.entity.Appointment;
import com.example.system.entity.Doctor;
import com.example.system.entity.Patient;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface AppointmentService {

    List<Appointment> getAllAppointments();
    Appointment getAppointmentById(Long id);
    AppointmentDTO getAppointment(Appointment appointment);
    void scheduleAppointment(Patient patient, Doctor doctor, LocalDate appointmentDate, String createdBy, int slotIndex);
    void UpdateAppointmentStatus(Appointment appointment, AppointmentStatus status);
    void cancelAppointment(Long id, String cancellationReason, String modifiedBy);
    void removeOldCanceledAppointments(Patient patient, Appointment appointment);
    List<Appointment> filterAppointments(LocalDate startDate, LocalDate endDate, AppointmentStatus status, Long doctorId);

    class AppointmentSpecifications{
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
}
