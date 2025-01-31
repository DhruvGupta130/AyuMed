package com.example.system.service.impl;

import com.example.system.dto.*;
import com.example.system.entity.*;
import com.example.system.entity.Doctor;
import com.example.system.entity.Patient;
import com.example.system.exception.HospitalManagementException;
import com.example.system.repository.*;
import com.example.system.service.FileService;
import com.example.system.service.PatientService;
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
import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepo patientRepo;
    private final AddressRepo addressRepo;
    private final MedicalHistoryRepo medicalHistoryRepo;
    private final AppointmentRepo appointmentRepo;
    private final UserRepo userRepo;
    private final ScheduleRepo scheduleRepo;
    private final MedicalTestRepo medicalTestRepo;
    private final PasswordEncoder passwordEncoder;
    private final PatientRecordRepo patientRecordRepo;
    private final FileService fileService;

    @Override
    @Transactional
    public void updatePatient(Patient patient, long aadhaarId, long mobile, MultipartFile image) {
        patient.setAadhaarId(aadhaarId);
        patient.setAlternateMobile(mobile);
        if (image != null && !image.isEmpty()) {
            try {
                File imageFile = fileService.saveFile(image);
                patient.setImage(imageFile.getFilePath());
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
    public Patient getPatientById(long id) {
        return patientRepo.findById(id)
                .orElseThrow(() -> new HospitalManagementException("Patient not found"));
    }


    @Override
    public void addAddress(Patient patient, Address address) {
        if(patient != null && address != null) {
            if(patient.getAddress()!=null) throw new HospitalManagementException("Address already exist");
            patient.setAddress(addressRepo.save(address));
            patientRepo.save(patient);
        }
    }

    private void addMedicalHistory(Patient patient, MedicalHistory medicalHistory) {
        medicalHistory.setPatient(patient);
        if (patient.getMedicalHistories() == null) {
            patient.setMedicalHistories(new ArrayList<>());
        }
        patient.getMedicalHistories().add(medicalHistory);
        patientRepo.save(patient);
    }

    @Override
    @Transactional
    public void addMedicalHistory(Doctor doctor, HistoryRequest historyRequest) {
        MedicalHistory medicalHistory = new MedicalHistory();
        Patient patient = this.getPatientById(historyRequest.getPatientId());
        medicalHistory.setProblems(historyRequest.getProblems());
        medicalHistory.setDiagnosisDetails(historyRequest.getDiagnosisDetails());
        medicalHistory.setMedications(historyRequest.getMedications());
        medicalHistory.setTreatmentPlan(historyRequest.getTreatmentPlan());
        medicalHistory.setFollowUpInstructions(historyRequest.getFollowUpInstructions());
        medicalHistory.setNotes(historyRequest.getNotes());
        medicalHistory.setDoctor(doctor);
        this.addMedicalHistory(patient, medicalHistory);

    }

    private void addLabResults(MedicalTest medicalTest, MedicalHistory medicalHistory) {
        medicalTest.setHistory(medicalHistory);
        if (medicalHistory.getTestsConducted() == null) {
            medicalHistory.setTestsConducted(new ArrayList<>());
        }
        medicalHistory.getTestsConducted().add(medicalTest);
        medicalHistoryRepo.save(medicalHistory);
    }

    @Override
    @Transactional
    public void addLabResults(MultipartFile file, LabTestRequest request) throws IOException {
        MedicalHistory history = medicalHistoryRepo.findById(request.getHistoryId())
                .orElseThrow(() -> new HospitalManagementException("Medical History not found"));
        String filePath = fileService.saveFile(file).getFilePath();
        MedicalTest test = new MedicalTest();
        test.setTestName(request.getTestName());
        test.setTestDate(request.getTestDate());
        test.setResult(request.getResult());
        test.setNotes(request.getNotes());
        test.setFilePath(filePath);
        this.addLabResults(test, history);
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
    public PatientDTO getPatientProfile(Patient patient) {
        return new PatientDTO(
                patient.getId(), patient.getFirstName(),
                patient.getLastName(), patient.getDateOfBirth(),
                patient.getGender(), patient.getEmail(),
                patient.getMobile(), patient.getAlternateMobile(),
                hideAadhaarId(patient.getAadhaarId()),
                patient.getImage(), patient.getFullName(),
                patient.getAddress()
        );
    }

    @Override
    public List<PatientDTO> getDoctorsPatient(Doctor doctor) {
        List<Patient> patients = patientRepo.findPatientsByDoctor(doctor);
        return patients.stream().map(this::getPatientProfile).toList();
    }

    @Override
    public List<HospitalPatientDTO> getHospitalPatient(Hospital hospital) {
        return patientRepo.findPatientsByHospital(hospital);
    }

    @Override
    public List<LabTestDTO> getMedicalTests(Patient patient) {
        List<MedicalTest> medicalTest = medicalTestRepo.findMedicalTestByPatient(patient);
        return medicalTest.stream().map(this::getLabTest).toList();
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
        return scheduleRepo.findAllByDateAndAvailableAndDoctor(date, doctor);
    }

    @Override
    public List<MedicalHistoryDTO> getMedicalHistory(Patient patient) {
        return patient.getMedicalHistories().stream().map(this::getMedicalHistory).toList();
    }

    private String hideAadhaarId(Long aadhaarId) {
        if(aadhaarId == null) return null;
        String aadhaarStr = Long.toString(aadhaarId);
        if (aadhaarStr.length() != 12) {
            throw new IllegalArgumentException("Invalid Aadhaar ID length");
        }
        return "XXXX XXXX XXXX " + aadhaarStr.substring(8);
    }

    private LabTestDTO getLabTest(MedicalTest medicalTest) {
        return new LabTestDTO(
                medicalTest.getId(), medicalTest.getTestName(),
                medicalTest.getTestDate(), medicalTest.getResult(),
                medicalTest.getNotes(), medicalTest.getFilePath()
        );
    }

    private MedicalHistoryDTO getMedicalHistory(MedicalHistory medicalHistory) {
        return new MedicalHistoryDTO(
                medicalHistory.getId(), medicalHistory.getProblems(),
                medicalHistory.getDiagnosisDetails(), medicalHistory.getMedications(),
                medicalHistory.getTreatmentStartDate(), medicalHistory.getLastTreatmentDate(),
                medicalHistory.getTreatmentPlan(), medicalHistory.getFollowUpInstructions(),
                medicalHistory.getPatient().getFullName(),
                medicalHistory.getDoctor().getFullName(),
                medicalHistory.getNotes()
        );
    }
}
