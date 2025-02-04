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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepo patientRepo;
    private final AddressRepo addressRepo;
    private final MedicalHistoryRepo medicalHistoryRepo;
    private final ScheduleRepo scheduleRepo;
    private final MedicalTestRepo medicalTestRepo;
    private final PasswordEncoder passwordEncoder;
    private final FileService fileService;


    @Override
    @Transactional
    public void updatePatient(Patient patient, String aadhaarId, String mobile, MultipartFile image) {
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
        this.updatePassword(patient, password.getNewPassword());
    }

    @Override
    public void updatePassword(Patient patient, String newPassword) {
        patient.getLoginUser().setPassword(passwordEncoder.encode(newPassword));
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

    @Override
    @Transactional
    public void addMedicalHistory(Doctor doctor, Patient patient, MedicalHistory history) {
        history.setPatient(patient);
        history.setDoctor(doctor);
        if (patient.getMedicalHistories() == null) {
            patient.setMedicalHistories(new ArrayList<>());
        }
        patient.getMedicalHistories().add(history);
        patientRepo.save(patient);
    }

    @Override
    @Transactional
    public void addLabResults(MedicalTest test, long historyId) {
        MedicalHistory history = medicalHistoryRepo.findById(historyId)
                .orElseThrow(() -> new HospitalManagementException("Medical History not found"));
        test.setHistory(history);
        if (history.getTestsConducted() == null) {
            history.setTestsConducted(new ArrayList<>());
        }
        history.getTestsConducted().add(test);
        medicalHistoryRepo.save(history);
    }

    @Override
    public MedicalHistory getMedicalHistoryById(long id) {
        return medicalHistoryRepo.getReferenceById(id);
    }

    @Override
    public PatientDTO getPatientProfile(Patient patient) {
        return new PatientDTO(
                patient.getId(), patient.getFirstName(),
                patient.getLastName(), patient.getDateOfBirth(),
                patient.getGender(), patient.getLoginUser().getEmail(),
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
    public List<PharmacyPatientsDTO> getPharmacyPatient(Pharmacy pharmacy){
        return patientRepo.findPatientByPharmacy(pharmacy);
    }

    @Override
    public List<LabTestDTO> getMedicalTests(Patient patient) {
        List<MedicalTest> medicalTest = medicalTestRepo.findMedicalTestByPatient(patient);
        return medicalTest.stream().map(this::getLabTest).toList();
    }

    @Override
    public List<LabTestDTO> getMedicalTests(MedicalHistory history) {
        return medicalTestRepo.findMedicalTestByHistory(history);
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
        LocalTime currentTimePlus30Minutes = LocalTime.now().plusMinutes(30);
        return scheduleRepo.findAllTimeSlotsDoctor(date, doctor, currentTimePlus30Minutes);
    }

    @Override
    public List<MedicalHistoryDTO> getMedicalHistory(Patient patient) {
        return patient.getMedicalHistories().stream().map(this::getMedicalHistory).toList();
    }

    private String hideAadhaarId(String aadhaarId) {
        if(aadhaarId == null) return null;
        if (aadhaarId.length() != 12) {
            throw new IllegalArgumentException("Invalid Aadhaar ID length");
        }
        return "XXXX XXXX XXXX " + aadhaarId.substring(8);
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
                medicalHistory.getTreatmentStartDate().toLocalDate(),
                medicalHistory.getLastTreatmentDate().toLocalDate(),
                medicalHistory.getTreatmentPlan(), medicalHistory.getFollowUpInstructions(),
                medicalHistory.getPatient().getFullName(),
                medicalHistory.getDoctor().getFullName(),
                medicalHistory.getNotes()
        );
    }
}
