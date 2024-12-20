package com.example.system.controller;

import com.example.system.dto.AvailableDTO;
import com.example.system.dto.DoctorDTO;
import com.example.system.dto.Gender;
import com.example.system.entity.*;
import com.example.system.entity.Doctor;
import com.example.system.entity.Patient;
import com.example.system.exception.HospitalManagementException;
import com.example.system.repository.*;
import com.example.system.service.DoctorService;
import com.example.system.service.PatientService;
import com.example.system.service.utils.Utility;
import io.jsonwebtoken.security.Password;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/patient")
@AllArgsConstructor
public class PatientController {

    private final Utility utility;
    private final DoctorRepo doctorRepo;
    private final DoctorService doctorService;
    private final ScheduleRepo scheduleRepo;
    private final PatientService patientService;

    @GetMapping("/profile")
    public ResponseEntity<Patient> getPatientProfile(@RequestHeader("Authorization") String token) {
        Patient patient = (Patient) utility.getUserFromToken(token);
        if (patient == null) throw new HospitalManagementException("Patient not found");
        return ResponseEntity.ok(patient);
    }

    @Transactional
    @PutMapping("/updateProfile")
    public ResponseEntity<String> updateProfile(@RequestHeader("Authorization") String token,
                                                @RequestParam("aadhaarId") long aadhaarId,
                                                @RequestParam("nationality") String nationality,
                                                @RequestParam("image") MultipartFile image,
                                                @RequestParam("mobile") long mobile) {
        Object object = utility.getUserFromToken(token);
        if(object instanceof Patient patient) {
            if (patient.getAadhaarId() != null) throw new HospitalManagementException("Profile is already up-to-date");
            patientService.updatePatient(patient, aadhaarId, mobile, nationality, image);
            return ResponseEntity.ok("Patient profile updated successfully");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Patient profile not found");
    }

    @PostMapping("/address")
    public ResponseEntity<String> addAddress(@RequestHeader("Authorization") String token, @RequestBody Address address){
        Object object = utility.getUserFromToken(token);
        if(object instanceof Patient patient){
            patientService.addAddress(patient, address);
        }
        return ResponseEntity.ok("Address added successfully");
    }

    @PutMapping("/address")
    public ResponseEntity<String> updateAddress(@RequestHeader("Authorization") String token, @RequestBody Address address) {
        Object object = utility.getUserFromToken(token);
        if (object instanceof Patient patient) {
            patientService.updateAddress(patient, address);
        }
        return ResponseEntity.ok("Address updated successfully!");
    }

    @GetMapping("/available")
    public ResponseEntity<String> isDoctorAvailable(@RequestBody AvailableDTO available) {
        Doctor doctor = doctorRepo.findById(available.getId()).orElseThrow(
                () -> new HospitalManagementException("Doctor not found"));
        Schedule schedule = scheduleRepo.findByDoctorAndDate(doctor, available.getDate())
                .orElseThrow(() -> new HospitalManagementException("Sorry this doctor is not available on the selected date"));
        if(schedule.hasAvailableSlots())
            return ResponseEntity.ok("Currently, only %s slots are available for the selected doctor."
                    .formatted(schedule.getAvailableSlotCount()));
        return ResponseEntity.ok("Unfortunately, there are no available slots for the selected doctor at this time.");
    }

    @GetMapping("/search")
    public ResponseEntity<List<DoctorDTO>> searchDoctors(@RequestParam(required = false) String specialty,
                                                         @RequestParam(required = false) Boolean available,
                                                         @RequestParam(required = false) String department) {
        List<Doctor> doctors = doctorService.searchDoctors(specialty, available, department);
        List<DoctorDTO> doctorDTOS = doctors.stream()
                .map(doctor -> new DoctorDTO(doctor.getFirstName(), doctor.getLastName(),
                        doctor.getSpecialty(), doctor.getDepartment(), doctor.getExperience(),
                        doctor.getImage(), doctor.getDegree())
                ).toList();
        return ResponseEntity.ok(doctorDTOS);
    }

    @Transactional
    @DeleteMapping("/delete")
    public ResponseEntity<String> deletePatient(@RequestHeader("Authorization") String token) {
        Patient patient = (Patient) utility.getUserFromToken(token);
        if(patient == null) throw new HospitalManagementException("Patient not found");
        patientService.deletePatient(patient);
        return ResponseEntity.ok("Patient deleted successfully.");
    }

    @GetMapping("/doctor")
    public ResponseEntity<List<DoctorDTO>> getDoctorsByDepartment(@RequestParam String department) {
        List<DoctorDTO> doctorDTOS = patientService.findDoctorsByDepartment(department);
        return ResponseEntity.ok(doctorDTOS);
    }

    @GetMapping("/lab")
    public ResponseEntity<List<MedicalTest>> getMedicalTests(@RequestHeader("Authorization") String token) {
        Patient patient = (Patient) utility.getUserFromToken(token);
        if (patient == null) throw new HospitalManagementException("Patient not found");
        return ResponseEntity.ok().body(patientService.getMedicalTests(patient));
    }

    @GetMapping("/slots/{id}")
    public ResponseEntity<List<TimeSlot>> getAvailableSlots(@RequestParam LocalDate date, @PathVariable long id) {
        return ResponseEntity.ok().body(patientService.getAvailableSlots(date,
                doctorService.getDoctorById(id)));
    }
}
