package com.example.system.service.impl;

import com.example.system.dto.DoctorDTO;
import com.example.system.dto.Password;
import com.example.system.entity.*;
import com.example.system.entity.Doctor;
import com.example.system.entity.Patient;
import com.example.system.exception.HospitalManagementException;
import com.example.system.repository.*;
import com.example.system.service.PatientService;
import com.example.system.service.utils.Utility;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private final MedicalTestRepo medicalTestRepo;
    private final PasswordEncoder passwordEncoder;
    private final PatientRecordRepo patientRecordRepo;
    private final Utility utility;

    @Override
    @Transactional
    public void updatePatient(Patient patient, long aadhaarId, long mobile, String nationality, MultipartFile image) {
        patient.setAadhaarId(aadhaarId);
        patient.setNationality(nationality);
        patient.setAlternateMobile(mobile);
        if (image != null && !image.isEmpty()) {
            try {
                String imagePath = utility.saveImage(image);
                patient.setImage(imagePath);
            } catch (Exception e) {
                throw new HospitalManagementException(e.getMessage());
            }
        }
        patientRepo.save(patient);
    }

    @Override
    public void updatePassword(Patient patient, Password password) {
        if(!passwordEncoder.matches(password.getOldPassword(), patient.getLoginUser().getPassword()))
            throw new HospitalManagementException("Provided password is incorrect");
        patient.getLoginUser().setPassword(passwordEncoder.encode(password.getNewPassword()));
        patientRepo.save(patient);
    }


    @Override
    public void addAddress(Patient patient, Address address) {
        if(patient != null && address != null) {
            if(patient.getAddress()!=null) throw new HospitalManagementException("Address already exist");
            patient.setAddress(addressRepo.save(address));
            patientRepo.save(patient);
        }
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
        List <PatientRecord> records = patient.getPatientRecords();
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
        }if (records != null) {
            records.forEach(record -> {
                record.setPatient(null);
                Path filePath = Paths.get(record.getFilePath());
                if (Files.exists(filePath)) {
                    try {
                        Files.delete(filePath);
                    } catch (IOException e) {
                        throw new HospitalManagementException(e.getMessage());
                    }
                }
            });
            patientRecordRepo.deleteAll(records);
        } if(patient.getImage() != null) {
            Path filePath = Paths.get(patient.getImage());
            if (Files.exists(filePath)) {
                try {
                    Files.delete(filePath);
                } catch (IOException e) {
                    throw new HospitalManagementException(e.getMessage());
                }
            }
        } if (user != null) {
            patient.setLoginUser(null);
            userRepo.delete(user);
        }
        patientRepo.delete(patient);
    }

    @Override
    public List<DoctorDTO> findDoctorsByDepartment(String department) {
        List<Doctor> doctors = doctorRepo.findAllByDepartment(department);
        return doctors.stream().map(doctor -> new DoctorDTO(doctor.getId(),
                doctor.getFirstName(), doctor.getLastName(),
                doctor.getSpecialty(), department,
                doctor.getExperience(), doctor.getImage(),
                doctor.getDegree(), doctor.getEmail())
        ).toList();
    }

    @Override
    public List<MedicalTest> getMedicalTests(Patient patient) {
        return medicalTestRepo.findMedicalTestByPatient(patient);
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
