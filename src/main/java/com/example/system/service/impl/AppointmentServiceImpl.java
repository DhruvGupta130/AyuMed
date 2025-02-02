package com.example.system.service.impl;

import com.example.system.dto.AppointmentDTO;
import com.example.system.dto.AppointmentStatus;
import com.example.system.dto.EmailStructures;
import com.example.system.entity.*;
import com.example.system.entity.Doctor;
import com.example.system.entity.Patient;
import com.example.system.exception.HospitalManagementException;
import com.example.system.repository.*;
import com.example.system.service.AppointmentService;
import com.example.system.service.utils.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@AllArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepo appointmentRepo;
    private final DoctorRepo doctorRepo;
    private final PatientRepo patientRepo;
    private final ScheduleRepo scheduleRepo;
    private final EmailService emailService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepo.findAll();
    }

    @Override
    public Appointment getAppointmentById(Long id) {
        return appointmentRepo.findById(id)
                .orElseThrow(() -> new HospitalManagementException("Appointment not found"));
    }

    @Override
    public AppointmentDTO getAppointment(Appointment appointment) {
        return new AppointmentDTO(
                appointment.getId(),
                appointment.getDoctor().getFullName(),
                appointment.getDoctor().getDepartment(),
                appointment.getDoctor().getImage(),
                appointment.getPatient().getFullName(),
                appointment.getPatient().getImage(),
                appointment.getPatient().getGender(),
                appointment.getPatient().getMobile(),
                appointment.getPatient().getEmail(),
                appointment.getDoctor().getHospital().getHospitalName(),
                appointment.getDoctor().getHospital().getAddress().toString(),
                appointment.getAppointmentDate(), appointment.getStatus(),
                appointment.getCancellationReason(), appointment.getSlotIndex()
        );
    }

    @Override
    public List<AppointmentDTO> getAllAppointmentsByDoctor(Doctor doctor) {
        return appointmentRepo.findAllByDoctor(doctor).stream().map(this::getAppointment).toList();
    }

    @Override
    public List<AppointmentDTO> getAllAppointmentsByPatient(Patient patient) {
        return appointmentRepo.findAllByPatient(patient).stream().map(this::getAppointment).toList();
    }

    @Override
    @Transactional
    public void scheduleAppointment(Patient patient, Doctor doctor,
                                    LocalDate appointmentDate, String createdBy, int slotIndex) {

        validateAppointmentDate(appointmentDate);
        Schedule schedule = getScheduleForDate(doctor, appointmentDate);
        ensureDoctorAvailability(schedule, appointmentDate.getDayOfWeek());

        Appointment appointment = createAppointment(patient, doctor, appointmentDate, slotIndex, createdBy, schedule);
        updateEntitiesWithAppointment(appointment, doctor, patient);
        sendAppointmentEmails(patient, doctor, appointmentDate, schedule, slotIndex);
    }

    private void validateAppointmentDate(LocalDate appointmentDate) {
        if (ChronoUnit.MONTHS.between(LocalDate.now(), appointmentDate) > 1) {
            throw new HospitalManagementException("Appointment cannot be scheduled more than one month in advance.");
        }
    }

    private Schedule getScheduleForDate(Doctor doctor, LocalDate date) {
        return scheduleRepo.findByDoctorAndDate(doctor, date)
                .orElseThrow(() -> new HospitalManagementException("Doctor is not available on the selected date"));
    }

    private void ensureDoctorAvailability(Schedule schedule, DayOfWeek appointmentDay) {
        if (!schedule.getDayOfWeek().equals(appointmentDay)) {
            throw new HospitalManagementException("The selected day is not available for the doctor.");
        }
    }

    private Appointment createAppointment(Patient patient, Doctor doctor, LocalDate date, int slotIndex, String createdBy, Schedule schedule) {
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(getAppointmentDate(date, slotIndex, schedule));
        appointment.setStatus(AppointmentStatus.BOOKED);
        appointment.setCreatedBy(createdBy);
        appointment.setLastModifiedBy(createdBy);
        appointment.setSlotIndex(slotIndex);
        appointmentRepo.save(appointment);
        schedule.bookSlot(slotIndex);
        scheduleRepo.save(schedule);
        return appointment;
    }

    private void updateEntitiesWithAppointment(Appointment appointment, Doctor doctor, Patient patient) {
        addAppointmentToDoctor(doctor, appointment);
        addAppointmentToPatient(patient, appointment);
    }

    private LocalDateTime getAppointmentDate(LocalDate date, int slotIndex, Schedule schedule) {
        return LocalDateTime.of(date, schedule.getAppointmentTime(slotIndex));
    }

    private void addAppointmentToDoctor(Doctor doctor, Appointment appointment) {
        doctor.getAppointments().add(appointment);
        doctorRepo.save(doctor);
    }

    private void addAppointmentToPatient(Patient patient, Appointment appointment) {
        patient.getAppointments().add(appointment);
        patientRepo.save(patient);
    }

    private void sendAppointmentEmails(Patient patient, Doctor doctor, LocalDate appointmentDate, Schedule schedule, int slotIndex) {
        String date = appointmentDate.format(DATE_FORMATTER);
        String time = schedule.getAppointmentTime(slotIndex).format(TIME_FORMATTER);
        String location = doctor.getLocation();

        emailService.sendEmail(patient.getEmail(), "Appointment Confirmation - AyuMed",
                EmailStructures.scheduleAppointmentPatientBody(patient.getFullName(), doctor.getFullName(), doctor.getSpecialty(), date, time, location));
        emailService.sendEmail(doctor.getEmail(), "New Appointment Scheduled - AyuMed",
                EmailStructures.scheduleAppointmentDoctorBody(doctor.getFullName(), patient.getFullName(), doctor.getDepartment(), date, time, location));
    }

    @Override
    public void UpdateAppointmentStatus(Appointment appointment, AppointmentStatus status) {
        if (appointment.getStatus().canTransitionTo(status)) {
            appointment.setStatus(status);
            appointmentRepo.save(appointment);
            sendStatusUpdateEmails(appointment, status);
        } else {
            throw new HospitalManagementException("Cannot transition from " + appointment.getStatus() + " to " + status);
        }
    }

    private void sendStatusUpdateEmails(Appointment appointment, AppointmentStatus status) {
        String date = appointment.getAppointmentDate().format(DATE_FORMATTER);
        String time = appointment.getAppointmentDate().format(TIME_FORMATTER);
        String location = appointment.getDoctor().getLocation();

        emailService.sendEmail(appointment.getPatient().getEmail(), "Appointment Status Updated - AyuMed",
                EmailStructures.appointmentStatusUpdatedPatient(appointment.getPatient().getFullName(), appointment.getDoctor().getFullName(), status.toString(), appointment.getDoctor().getSpecialty(), date, time, location));
        emailService.sendEmail(appointment.getDoctor().getEmail(), "Appointment Status Updated Successfully - AyuMed",
                EmailStructures.appointmentStatusUpdatedDoctor(appointment.getDoctor().getFullName(), appointment.getPatient().getFullName(), date, time, status.toString()));
    }

    @Override
    @Transactional
    public void cancelAppointment(Long id, String cancellationReason, String modifiedBy) {
        Appointment appointment = getAppointmentById(id);
        if (isAlreadyCancelled(appointment) || isCompleted(appointment)) throw new HospitalManagementException("This appointment cannot be canceled.");
        if (appointment.getAppointmentDate().isBefore(LocalDateTime.now()))
            throw new HospitalManagementException("This appointment is expired.");
        restoreScheduleSlot(appointment);
        appointment.setStatus(AppointmentStatus.CANCELED);
        appointment.setLastModifiedBy(modifiedBy);
        appointment.setCancellationReason(cancellationReason);
        appointmentRepo.save(appointment);
        sendCancellationEmails(appointment, cancellationReason);
    }

    private void restoreScheduleSlot(Appointment appointment) {
        LocalDateTime appointmentDate = appointment.getAppointmentDate();
        if (appointmentDate.isAfter(LocalDateTime.now())) {
            scheduleRepo.findByDoctorAndDate(appointment.getDoctor(), appointmentDate.toLocalDate())
                    .ifPresent(schedule -> schedule.restoreSlot(appointment.getSlotIndex()));
        }
    }

    private void sendCancellationEmails(Appointment appointment, String reason) {
        String date = appointment.getAppointmentDate().format(DATE_FORMATTER);
        String time = appointment.getAppointmentDate().format(TIME_FORMATTER);
        String location = appointment.getDoctor().getLocation();

        emailService.sendEmail(appointment.getPatient().getEmail(), "Appointment Cancelled - AyuMed",
                EmailStructures.cancelAppointmentPatient(appointment.getPatient().getFullName(), appointment.getDoctor().getFullName(), appointment.getDoctor().getSpecialty(), date, time, location, reason));
        emailService.sendEmail(appointment.getDoctor().getEmail(), "Appointment Successfully Cancelled - AyuMed",
                EmailStructures.cancelAppointmentDoctor(appointment.getPatient().getFullName(), appointment.getDoctor().getFullName(), appointment.getDoctor().getDepartment(), date, time, location, reason));
    }

    private boolean isAlreadyCancelled(Appointment appointment) {
        return appointment.getStatus() == AppointmentStatus.CANCELED;
    }
    private boolean isCompleted(Appointment appointment) {
        return appointment.getStatus() == AppointmentStatus.COMPLETED;
    }

    @Override
    @Transactional
    public void removeOldCanceledAppointments(Patient patient, Appointment appointment) {
        if (!appointment.getPatient().getId().equals(patient.getId())) {
            throw new HospitalManagementException("You are not authorized to perform this action.");
        }
        if (appointment.getStatus() == AppointmentStatus.CANCELED &&
                ChronoUnit.MONTHS.between(appointment.getAppointmentDate(), LocalDateTime.now()) > 1){
            appointmentRepo.delete(appointment);
        }
        throw new HospitalManagementException("Only cancelled and older appointments can be removed.");
    }

    @Override
    public List<Appointment> filterAppointments(LocalDate startDate, LocalDate endDate, AppointmentStatus status, Long doctorId) {
        Specification<Appointment> spec = buildFilterSpecification(startDate, endDate, status, doctorId);
        return appointmentRepo.findAll(spec);
    }

    private Specification<Appointment> buildFilterSpecification(LocalDate startDate, LocalDate endDate, AppointmentStatus status, Long doctorId) {
        Specification<Appointment> spec = Specification.where(null);
        if (startDate != null && endDate != null) spec = spec.and(AppointmentSpecifications.betweenDates(startDate, endDate.plusDays(1)));
        if (status != null) spec = spec.and(AppointmentSpecifications.hasStatus(status));
        if (doctorId != null) spec = spec.and(AppointmentSpecifications.byDoctorId(doctorId));
        return spec;
    }
}
