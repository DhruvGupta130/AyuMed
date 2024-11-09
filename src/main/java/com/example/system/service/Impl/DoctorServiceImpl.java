package com.example.system.service.Impl;

import com.example.system.dto.DoctorDTO;
import com.example.system.dto.ProfileUpdateDTO;
import com.example.system.entity.*;
import com.example.system.exception.HospitalManagementException;
import com.example.system.repository.*;
import com.example.system.service.DoctorService;
import com.example.system.service.SlotInitializationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepo doctorRepo;
    private final UserRepo userRepo;
    private final ScheduleRepo scheduleRepo;
    private final AppointmentRepo appointmentRepo;
    private final TimeSlotRepo timeSlotRepo;
    private final SlotInitializationService slotInitializationService;

    @Override
    public Doctor getDoctorById(long id) {
        return doctorRepo.getDoctorById(id).orElseThrow(() -> new HospitalManagementException("Doctor not found"));
    }

    @Override
    public void deleteProfile(Doctor doctor) {
        LoginUser user = doctor.getLoginUser();
        Hospital hospital = doctor.getHospital();
        List<Schedule> schedules = doctor.getSchedules();
        List<Appointment> appointments = doctor.getAppointments();
        if (user != null) {
            doctor.setLoginUser(null);
            userRepo.delete(user);
        }if(hospital != null) {
            hospital.getDoctors().remove(doctor);
            doctor.setHospital(null);
        }if (schedules != null) {
            schedules.forEach(s -> {
                s.getTimeSlots().forEach(t -> t.setSchedule(null));
                timeSlotRepo.deleteAll(s.getTimeSlots());
                s.getTimeSlots().clear();
                s.setDoctor(null);
                scheduleRepo.save(s);
            });
            scheduleRepo.deleteAll(schedules);
            doctor.getSchedules().clear();
        }if(appointments != null) {
            appointments.forEach(appointment -> {
                appointment.setDoctor(null);
                if(appointment.getPatient() != null)
                    appointment.getPatient().getAppointments().remove(appointment);
                appointment.setPatient(null);
                appointmentRepo.save(appointment);
            });
            appointmentRepo.deleteAll(appointments);
        }
        doctorRepo.delete(doctor);
    }

    @Override
    public List<Doctor> searchDoctors(String specialty, Boolean available, String department) {
        return doctorRepo.searchDoctorsByKeyword(specialty, available, department);
    }

    @Override
    public void updateDoctor(Doctor doctor, ProfileUpdateDTO updateDTO) {
        doctor.setStartDate(updateDTO.getStartDate());
        doctor.setDegree(updateDTO.getDegree());
        doctor.setImage(updateDTO.getImage());
        slotInitializationService.initializeAvailableSlots(doctor, updateDTO.getSchedule());
        doctorRepo.save(doctor);
    }

    @Override
    public List<DoctorDTO> getDoctorBySearch(String keyword) {
        List<Doctor> doctors = doctorRepo.searchDoctorsByKeyword(keyword);
        return doctors.stream().map(doctor ->
                new DoctorDTO(doctor.getFirstName(), doctor.getLastName(),
                        doctor.getSpecialty(), doctor.getDepartment(),
                        doctor.getExperience(), doctor.getImage(), doctor.getDegree())
        ).toList();
    }

}
