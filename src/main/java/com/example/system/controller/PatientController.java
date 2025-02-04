package com.example.system.controller;

import com.example.system.dto.*;
import com.example.system.entity.*;
import com.example.system.entity.Doctor;
import com.example.system.entity.Patient;
import com.example.system.exception.HospitalManagementException;
import com.example.system.repository.*;
import com.example.system.service.DoctorService;
import com.example.system.service.HospitalService;
import com.example.system.service.PatientService;
import com.example.system.service.PharmacyService;
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
import java.util.Map;

@RestController
@RequestMapping("/api/patient")
@AllArgsConstructor
public class PatientController {

    private final Utility utility;
    private final DoctorRepo doctorRepo;
    private final DoctorService doctorService;
    private final ScheduleRepo scheduleRepo;
    private final PatientService patientService;
    private final HospitalService hospitalService;
    private final PharmacyService pharmacyService;

    @GetMapping("/profile")
    public ResponseEntity<PatientDTO> getPatientProfile(@RequestHeader("Authorization") String token) {
        Patient patient = (Patient) utility.getUserFromToken(token);
        return ResponseEntity.ok(patientService.getPatientProfile(patient));
    }

    @Transactional
    @PutMapping("/updateProfile")
    public ResponseEntity<Response> updateProfile(@RequestHeader("Authorization") String token,
                                                  @RequestParam("aadhaarId") String aadhaarId,
                                                  @RequestParam("image") MultipartFile image,
                                                  @RequestParam("mobile") String mobile) {
        Response response = new Response();
        try {
            Patient patient = (Patient) utility.getUserFromToken(token);
            if (patient.getAadhaarId() != null)
                throw new HospitalManagementException("Profile is already up-to-date");
            patientService.updatePatient(patient, aadhaarId, mobile, image);
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
        List<DoctorDTO> doctors = doctorService.searchDoctors(specialty, available, department);
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/doctor")
    public ResponseEntity<List<DoctorDTO>> getDoctorsByDepartment(@RequestParam String department) {
        List<DoctorDTO> doctorDTOS = doctorService.getDoctorsByDepartment(department);
        return ResponseEntity.ok(doctorDTOS);
    }

    @GetMapping("/departments")
    public ResponseEntity<List<String>> getDepartments() {
        return ResponseEntity.ok(hospitalService.getAllDepartments());
    }

    @GetMapping("/hospital/{hospitalId}")
    public ResponseEntity<HospitalDTO> getHospital(@PathVariable long hospitalId) {
        return ResponseEntity.ok(hospitalService.getHospitalDtoById(hospitalId));
    }

    @GetMapping("/pharmacy/{pharmacyId}")
    public ResponseEntity<PharmacyDTO> getPharmacy(@PathVariable long pharmacyId) {
        return ResponseEntity.ok(pharmacyService.getPharmacyById(pharmacyId));
    }

    @GetMapping("/{department}/hospitals")
    public ResponseEntity<List<HospitalDTO>> getHospitalsByDepartment(@PathVariable String department) {
        return ResponseEntity.ok(hospitalService.getHospitalsByDepartment(department));
    }

    @GetMapping("/{department}/{hospitalId}/doctors")
    public ResponseEntity<List<DoctorDTO>> getDoctorsByDepartmentAndHospital(@PathVariable String department,
                                                                             @PathVariable long hospitalId) {
        Hospital hospital = hospitalService.getHospitalById(hospitalId);
        return ResponseEntity.ok(doctorService.getDoctorsByHospitalAndDepartment(hospital, department));
    }

    @GetMapping("/{doctorId}/doctor")
    public ResponseEntity<DoctorDTO> getDoctorById(@PathVariable long doctorId) {
        return ResponseEntity.ok(doctorService.getDoctorDTOById(doctorId));
    }

    @GetMapping("/{hospitalId}/doctors")
    public ResponseEntity<List<DoctorDTO>> getDoctorsByHospital(@PathVariable long hospitalId) {
        Hospital hospital = hospitalService.getHospitalById(hospitalId);
        return ResponseEntity.ok(doctorService.getDoctorsByHospital(hospital));
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorDTO>> getDoctors() {
        return ResponseEntity.ok(doctorService.getAllTheDoctors());
    }

    @GetMapping("/hospitals")
    public ResponseEntity<List<HospitalDTO>> getHospitals(@RequestHeader("Authorization") String token,
                                                          @RequestParam long radius) {
        Patient patient = (Patient) utility.getUserFromToken(token);
        if(patient.getAddress() == null) throw new HospitalManagementException("Please update the address first");
        List<HospitalDTO> hospitalDTOS = hospitalService.getHospitalsWithinRadius(
                patient.getAddress().getLatitude(), patient.getAddress().getLongitude(),radius
        );
        return ResponseEntity.ok(hospitalDTOS);
    }

    @GetMapping("/lab")
    public ResponseEntity<List<LabTestDTO>> getMedicalTests(@RequestHeader("Authorization") String token) {
        Patient patient = (Patient) utility.getUserFromToken(token);
        return ResponseEntity.ok().body(patientService.getMedicalTests(patient));
    }

    @GetMapping("/{doctorId}/slots")
    public ResponseEntity<List<TimeSlot>> getAvailableSlots(@RequestParam LocalDate date,
                                                            @PathVariable long doctorId) {
        return ResponseEntity.ok().body(patientService.getAvailableSlots(date,
                doctorService.getDoctorById(doctorId)));
    }

    @GetMapping("/medical-histories")
    public ResponseEntity<List<MedicalHistoryDTO>> getMedicalHistories(@RequestHeader("Authorization") String token) {
        Patient patient = (Patient) utility.getUserFromToken(token);
        if(patient.getMedicalHistories() == null) throw new HospitalManagementException("No medical histories found");
        return ResponseEntity.ok(patientService.getMedicalHistory(patient));
    }

    @GetMapping("/medications/all")
    public ResponseEntity<List<MedicationDTO>> getMedications() {
        return ResponseEntity.ok().body(pharmacyService.getMedications());
    }

    @GetMapping("/{pharmacyId}/medications")
    public ResponseEntity<List<MedicationDTO>> getMedicationsByPharmacy(@PathVariable long pharmacyId) {
        return ResponseEntity.ok(pharmacyService.getMedicationsByPharmacy(pharmacyId));
    }

    @GetMapping("/medications")
    public ResponseEntity<List<MedicationDTO>> getMedications(@RequestHeader("Authorization") String token) {
        Patient patient = (Patient) utility.getUserFromToken(token);
        return ResponseEntity.ok(pharmacyService.getPatientMedications(patient));
    }

    @GetMapping("/{medicationId}/medication")
    public ResponseEntity<MedicationDTO> getMedicationById(@PathVariable long medicationId) {
        return ResponseEntity.ok(pharmacyService.getMedicationById(medicationId));
    }

    @PostMapping("/medications/buy")
    public ResponseEntity<Response> buyMedications(@RequestHeader("Authorization") String token, @RequestBody Map<Long, Long> medicationOrders) {
        Patient patient = (Patient) utility.getUserFromToken(token);
        Response response = new Response();
        try{
            pharmacyService.buyMedications(patient, medicationOrders);
            response.setMessage("Successfully bought medications");
            response.setStatus(HttpStatus.OK);
        } catch (HospitalManagementException e) {
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.setMessage("Error while adding medication" + e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
