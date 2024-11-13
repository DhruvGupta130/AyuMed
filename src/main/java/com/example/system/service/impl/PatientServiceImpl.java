package com.example.system.service.impl;

import com.example.system.dto.DoctorDTO;
import com.example.system.dto.ProfileUpdateDTO;
import com.example.system.entity.*;
import com.example.system.entity.Doctor;
import com.example.system.entity.Patient;
import com.example.system.repository.*;
import com.example.system.service.PatientService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepo patientRepo;
    private final AddressRepo addressRepo;
    private final MedicalHistoryRepo medicalHistoryRepo;
    private final AppointmentRepo appointmentRepo;
    private final UserRepo userRepo;
    private final DoctorRepo doctorRepo;
    private final ScheduleRepo scheduleRepo;

    @Override
    @Transactional
    public void updatePatient(Patient patient, ProfileUpdateDTO updateDTO) {
        patient.setAadhaarId(updateDTO.getAadhaarId());
        patient.setNationality(updateDTO.getNationality());
        patient.setAddress(addressRepo.save(updateDTO.getAddress()));
        patient.setImage(updateDTO.getImage());
        this.addMedicalHistory(patient, updateDTO.getMedicalHistories());
        patientRepo.save(patient);
    }

    @Override
    public void addMedicalHistory(Patient patient, List<MedicalHistory> medicalHistories) {
        medicalHistories.forEach(medicalHistory -> {
            medicalHistory.setPatient(patient);
            medicalHistory.getTestsConducted().forEach(
                    testConducted -> testConducted.setHistory(medicalHistory));
        });
        if(patient.getMedicalHistories() != null)
            patient.getMedicalHistories().addAll(medicalHistories);
        else
            patient.setMedicalHistories(medicalHistories);
    }

    @Override
    @Transactional
    public void deletePatient(Patient patient) {
        LoginUser user = patient.getLoginUser();
        Address address = patient.getAddress();
        List<Appointment> appointments = patient.getAppointments();
        List<MedicalHistory> medicalHistories = patient.getMedicalHistories();
        if (medicalHistories != null) {
            medicalHistories.forEach(medicalHistory -> medicalHistory.setPatient(null));
            medicalHistoryRepo.deleteAll(medicalHistories);
        }if(appointments != null) {
            appointments.forEach(appointment -> {
                appointment.setPatient(null);
                if(appointment.getDoctor() != null)
                    appointment.getDoctor().getAppointments().remove(appointment);
                appointment.setDoctor(null);
                appointmentRepo.save(appointment);
            });
            appointmentRepo.deleteAll(appointments);
        }if (address != null) {
            patient.setAddress(null);
            addressRepo.delete(address);
        }if (user != null) {
            patient.setLoginUser(null);
            userRepo.delete(user);
        }
        patientRepo.delete(patient);
    }

    @Override
    public List<DoctorDTO> findDoctorsByDepartment(String department) {
        List<Doctor> doctors = doctorRepo.findAllByDepartment(department);
        return doctors.stream().map(doctor -> new DoctorDTO(
                doctor.getFirstName(), doctor.getLastName(),
                doctor.getSpecialty(), department,
                doctor.getExperience(), doctor.getImage(),
                doctor.getDegree())
        ).toList();
    }

    @Override
    public void updateAddress(Patient patient, Address addresses) {
        Address address = addressRepo.findById(patient.getAddress().getId())
                .orElseThrow(() -> new RuntimeException("Address not found"));
        addresses.setId(address.getId());
        addressRepo.save(addresses);
    }

    @Override
    public List<TimeSlot> getAvailableSlots(LocalDate date, Doctor doctor){
        return scheduleRepo.findAllByDateAndAvailableAAndDoctor(date, doctor);
    }
}
