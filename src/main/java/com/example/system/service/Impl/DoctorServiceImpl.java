package com.example.system.service.Impl;

import com.example.system.dto.*;
import com.example.system.entity.*;
import com.example.system.exception.HospitalManagementException;
import com.example.system.repository.*;
import com.example.system.service.AuthService;
import com.example.system.service.DoctorService;
import com.example.system.service.SlotInitializationService;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
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
    private final AuthService authService;

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

    @Override
    public void saveFromExcel(MultipartFile file, long hospitalId) throws HospitalManagementException {
        try (InputStream is = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            if (rows.hasNext()) rows.next();
            while (rows.hasNext()) {
                Row row = rows.next();
                RegistrationDTO registrationDTO = createRegistrationDTO(row, hospitalId);
                System.out.println(registrationDTO);
                authService.createDoctor(registrationDTO);
            }
        } catch (IOException e) {
            throw new HospitalManagementException("Error reading the Excel file: " + e.getMessage(), e);
        }
    }

    private RegistrationDTO createRegistrationDTO(Row row, long hospitalId) {
        try {
            RegistrationDTO registrationDTO = new RegistrationDTO();
            registrationDTO.setUsername(getCellValue(row, 0));
            registrationDTO.setPassword(getCellValue(row, 1));
            registrationDTO.setRole(UserRole.ROLE_DOCTOR);
            registrationDTO.setFirstName(getCellValue(row, 2));
            registrationDTO.setLastName(getCellValue(row, 3));
            registrationDTO.setGender(parseGender(getCellValue(row, 4)));
            registrationDTO.setEmail(getCellValue(row, 5));
            registrationDTO.setMobile(getCellValue(row, 6));
            registrationDTO.setDepartment(getCellValue(row, 7));
            registrationDTO.setSpecialty(getCellValue(row, 8));
            registrationDTO.setLicenseNumber(getCellValue(row, 9));
            registrationDTO.setHospitalId(hospitalId);
            return registrationDTO;
        } catch (Exception e) {
            throw new HospitalManagementException("Error creating registrationDTO: " + e.getMessage(), e);
        }
    }

    private String getCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        return switch (cell.getCellType()){
                case NUMERIC-> throw new HospitalManagementException("Numerical Value not supported, please convert it in text.");
                case STRING -> cell.getStringCellValue().trim();
                default -> throw new HospitalManagementException("Your Excel File structure is incorrect.");
        };
    }

    private Gender parseGender(String genderString) {
        if (genderString != null && !genderString.isEmpty()) {
            try {
                return Gender.valueOf(genderString.toUpperCase());
            } catch (Exception _) {}
        }
        return Gender.OTHER;
    }
}
