package com.example.system.controller;

import com.example.system.dto.*;
import com.example.system.entity.Doctor;
import com.example.system.entity.MedicalHistory;
import com.example.system.entity.Patient;
import com.example.system.entity.Schedule;
import com.example.system.exception.HospitalManagementException;
import com.example.system.service.DoctorService;
import com.example.system.service.HospitalService;
import com.example.system.service.PatientService;
import com.example.system.service.utils.Utility;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor")
@AllArgsConstructor
public class DoctorController {

    private final Utility utility;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final HospitalService hospitalService;

    @GetMapping
    public ResponseEntity<DoctorDTO> getDoctorProfile(@RequestHeader("Authorization") String token) {
        Doctor doctor = (Doctor) utility.getUserFromToken(token);
        return ResponseEntity.ok(doctorService.getDoctorProfile(doctor));
    }

    @Transactional
    @PutMapping("/updateProfile")
    public ResponseEntity<Response> updateProfile(@RequestHeader("Authorization") String token,
                                                  @RequestBody DoctorUpdateDTO doctorDTO) {
        Response response = new Response();
        Object user = utility.getUserFromToken(token);
        if (!(user instanceof Doctor doctor))
            throw new HospitalManagementException("Unauthorized access: User is not a doctor.");
        try {
            doctorService.updateDoctor(doctor, doctorDTO);
            response.setMessage("Profile updated successfully");
            response.setStatus(HttpStatus.OK);
        } catch (HospitalManagementException e) {
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.setMessage("Error updating profile: " + e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/schedules")
    public ResponseEntity<List<ScheduleDTO>> getDoctorSchedules(@RequestHeader("Authorization") String token,
                                                                @RequestParam(required = false) String sortBy,
                                                                @RequestParam(required = false) String sortDirection) {
        Doctor doctor = (Doctor) utility.getUserFromToken(token);
        return ResponseEntity.ok(doctorService.getSchedules(doctor, sortBy, sortDirection));
    }

    @PostMapping("/schedules")
    public ResponseEntity<Response> addSchedule(@RequestHeader("Authorization") String token,
                                                @RequestBody List<Schedule> schedules) {
        Response response = new Response();
        try {
            Doctor doctor = (Doctor) utility.getUserFromToken(token);
            List<Schedule> existingSchedules = schedules.stream().filter(s -> s.getId() != 0).toList();
            List<Schedule> newSchedules = schedules.stream().filter(s -> s.getId() == 0).toList();
            doctorService.addSchedule(doctor, newSchedules);
            doctorService.updateSchedule(doctor, existingSchedules);
            response.setMessage("Schedule updated successfully");
            response.setStatus(HttpStatus.OK);
        } catch (HospitalManagementException e) {
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.setMessage("Error updating schedule: " + e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/schedules")
    public ResponseEntity<Response> deleteSchedules(@RequestHeader("Authorization") String token, @RequestBody List<Schedule> schedules) {
        Response response = new Response();
        try{
            Doctor doctor = (Doctor) utility.getUserFromToken(token);
            if(schedules.isEmpty())
                doctorService.deleteAllSchedules(doctor);
            else {
                List<Long> scheduleIds = schedules.stream().map(Schedule::getId).toList();
                doctorService.deleteSelectedSchedules(doctor, scheduleIds);
            }
            response.setMessage("Schedules deleted successfully");
            response.setStatus(HttpStatus.OK);
        } catch (HospitalManagementException e) {
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.setMessage("Error deleting schedules: " + e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/patients")
    public ResponseEntity<List<PatientDTO>> getDoctorPatients(@RequestHeader("Authorization") String token) {
        Doctor doctor = (Doctor) utility.getUserFromToken(token);
        return ResponseEntity.ok(patientService.getDoctorsPatient(doctor));
    }

    @GetMapping("/medical-histories/{patientId}")
    public ResponseEntity<List<MedicalHistoryDTO>> getMedicalHistories(@PathVariable long patientId) {
        return ResponseEntity.ok(hospitalService.getPatientsMedicalHistory(patientId));
    }

    @PostMapping("/add-medical-history/{patientId}")
    public ResponseEntity<Response> addMedicalHistory(@RequestHeader("Authorization") String token,
                                                      @PathVariable long patientId,
                                                      @RequestBody MedicalHistory history) {
        System.out.println(history);
        Response response = new Response();
        try {
            Doctor doctor = (Doctor) utility.getUserFromToken(token);
            Patient patient = patientService.getPatientById(patientId);
            patientService.addMedicalHistory(doctor, patient, history);
            response.setMessage("Medical History added successfully.");
            response.setStatus(HttpStatus.CREATED);
        } catch (HospitalManagementException e) {
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.setMessage("Error in adding Medical History: " + e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/lab-tests/{medicalId}")
    public ResponseEntity<List<LabTestDTO>> getMedicalTests(@PathVariable long medicalId) {
        return ResponseEntity.ok().body(hospitalService.getPatientLabResults(medicalId));
    }

    @GetMapping("/hospital")
    public ResponseEntity<HospitalDTO> getHospital(@RequestHeader("Authorization") String token) {
        Doctor doctor = (Doctor) utility.getUserFromToken(token);
        HospitalDTO hospitalDTO = hospitalService.getHospitalProfile(doctor.getHospital());
        return ResponseEntity.ok(hospitalDTO);
    }

}
