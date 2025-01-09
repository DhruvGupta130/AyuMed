package com.example.system.controller;

import com.example.system.dto.DoctorDTO;
import com.example.system.dto.PatientDTO;
import com.example.system.dto.Response;
import com.example.system.entity.*;
import com.example.system.entity.Doctor;
import com.example.system.entity.Patient;
import com.example.system.exception.HospitalManagementException;
import com.example.system.repository.*;
import com.example.system.service.DoctorService;
import com.example.system.service.PatientService;
import com.example.system.service.utils.Utility;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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
    public ResponseEntity<PatientDTO> getPatientProfile(@RequestHeader("Authorization") String token) {
        Patient patient = (Patient) utility.getUserFromToken(token);
        return ResponseEntity.ok(patientService.getPatientProfile(patient));
    }

    @Transactional
    @PutMapping("/updateProfile")
    public ResponseEntity<Response> updateProfile(@RequestHeader("Authorization") String token,
                                                  @RequestParam("aadhaarId") long aadhaarId,
                                                  @RequestParam("nationality") String nationality,
                                                  @RequestParam("image") MultipartFile image,
                                                  @RequestParam("mobile") long mobile) {
        Response response = new Response();
        try {
            Patient patient = (Patient) utility.getUserFromToken(token);
            if (patient.getAadhaarId() != null)
                throw new HospitalManagementException("Profile is already up-to-date");
            patientService.updatePatient(patient, aadhaarId, mobile, nationality, image);
            response.setMessage("Profile successfully updated");
            response.setStatus(HttpStatus.OK);
        } catch (HospitalManagementException e) {
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.setMessage("Error while updating profile " + e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/address")
    public ResponseEntity<Response> addAddress(@RequestHeader("Authorization") String token,
                                               @RequestBody Address address){
        Response response = new Response();
        try {
            Patient patient = (Patient) utility.getUserFromToken(token);
            patientService.addAddress(patient, address);
            response.setMessage("Address successfully added");
            response.setStatus(HttpStatus.OK);
        } catch (HospitalManagementException e) {
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.setMessage("Error while adding address " + e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/address")
    public ResponseEntity<Response> updateAddress(@RequestHeader("Authorization") String token,
                                                  @RequestBody Address address) {
        Response response = new Response();
        try {
        Patient patient = (Patient) utility.getUserFromToken(token);
        patientService.updateAddress(patient, address);
        response.setMessage("Address successfully updated");
        response.setStatus(HttpStatus.OK);
        } catch (HospitalManagementException e) {
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.setMessage("Error while updating address " + e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/available")
    public ResponseEntity<Response> isDoctorAvailable(
            @RequestParam("id") long id,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        Response response = new Response();
        try {
            Doctor doctor = doctorRepo.findById(id).orElseThrow(
                    () -> new HospitalManagementException("Doctor not found"));
            Schedule schedule = scheduleRepo.findByDoctorAndDate(doctor, date).orElseThrow(
                    () -> new HospitalManagementException("The doctor is not available on the selected date: " + date));

            if (schedule.hasAvailableSlots()) {
                response.setStatus(HttpStatus.OK);
                response.setMessage(String.format("Currently, only %d slots are available for Dr. %s on %s.",
                        schedule.getAvailableSlotCount(), doctor.getFullName(), date));
            } else {
                throw new HospitalManagementException("No slots are available for Dr. " + doctor.getFullName() + " on " + date);
            }
        } catch (HospitalManagementException e) {
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.setMessage("An unexpected error occurred: " + e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<DoctorDTO>> searchDoctors(@RequestParam(required = false) String specialty,
                                                         @RequestParam(required = false) Boolean available,
                                                         @RequestParam(required = false) String department) {
        List<Doctor> doctors = doctorService.searchDoctors(specialty, available, department);
        List<DoctorDTO> doctorDTOS = doctors.stream()
                .map(doctorService::getDoctorProfile).toList();
        return ResponseEntity.ok(doctorDTOS);
    }

    @Transactional
    @DeleteMapping("/delete")
    public ResponseEntity<Response> deletePatient(@RequestHeader("Authorization") String token) {
        Response response = new Response();
        try {
            Patient patient = (Patient) utility.getUserFromToken(token);
            patientService.deletePatient(patient);
            response.setMessage("Patient successfully deleted");
            response.setStatus(HttpStatus.OK);
        } catch (HospitalManagementException e) {
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.setMessage("Error while deleting patient " + e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/doctor")
    public ResponseEntity<List<DoctorDTO>> getDoctorsByDepartment(@RequestParam String department) {
        List<DoctorDTO> doctorDTOS = patientService.findDoctorsByDepartment(department);
        return ResponseEntity.ok(doctorDTOS);
    }

    @GetMapping("/lab")
    public ResponseEntity<List<MedicalTest>> getMedicalTests(@RequestHeader("Authorization") String token) {
        Patient patient = (Patient) utility.getUserFromToken(token);
        return ResponseEntity.ok().body(patientService.getMedicalTests(patient));
    }

    @GetMapping("/slots/{id}")
    public ResponseEntity<List<TimeSlot>> getAvailableSlots(@RequestParam LocalDate date, @PathVariable long id) {
        return ResponseEntity.ok().body(patientService.getAvailableSlots(date,
                doctorService.getDoctorById(id)));
    }

    @GetMapping("/medical-histories")
    public ResponseEntity<List<MedicalHistory>> getMedicalHistories(@RequestHeader("Authorization") String token) {
        Patient patient = (Patient) utility.getUserFromToken(token);
        return ResponseEntity.ok(patient.getMedicalHistories());
    }
}
