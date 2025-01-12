package com.example.system.service.impl;

import com.example.system.dto.*;
import com.example.system.entity.*;
import com.example.system.entity.Doctor;
import com.example.system.exception.HospitalManagementException;
import com.example.system.repository.*;
import com.example.system.service.AuthService;
import com.example.system.service.DoctorService;
import com.example.system.service.FileService;
import com.example.system.service.SlotInitializationService;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
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
    private final PasswordEncoder passwordEncoder;
    private final FileService fileService;

    @Override
    public Doctor getDoctorById(long id) {
        return doctorRepo.findById(id)
                .orElseThrow(() -> new HospitalManagementException("Doctor not found"));
    }

    @Override
    @Transactional
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
    public DoctorDTO getDoctorProfile(Doctor doctor) {
        return new DoctorDTO(
                doctor.getId(), doctor.getFirstName(),
                doctor.getLastName(), doctor.getGender(),
                doctor.getEmail(), doctor.getMobile(),
                doctor.getSpecialty(), doctor.getLicenseNumber(),
                doctor.getDepartment(), doctor.getExperience(),
                doctor.getImage(), doctor.getDegree()
        );
    }

    @Override
    public List<DoctorDTO> searchDoctors(String specialty, Boolean available, String department) {
        List<Doctor> doctors = doctorRepo.searchDoctorsByKeyword(specialty, available, department);
        return doctors.stream().map(this::getDoctorProfile).toList();
    }

    @Override
    @Transactional
    public void updateDoctor(Doctor doctor, ProfileUpdateDTO updateDTO, MultipartFile image) {
        doctor.setStartDate(updateDTO.getStartDate());
        doctor.setDegree(updateDTO.getDegree());
        if (image != null && !image.isEmpty()) {
            try {
                String imagePath = fileService.saveFile(image).getFilePath();
                doctor.setImage(imagePath);
            } catch (Exception e) {
                throw new HospitalManagementException(e.getMessage());
            }
        }
        slotInitializationService.initializeAvailableSlots(doctor, updateDTO.getSchedule());
        doctorRepo.save(doctor);
    }

    @Override
    public void updatePassword(Doctor doctor, Password password) {
        if(!passwordEncoder.matches(password.getOldPassword(), doctor.getLoginUser().getPassword()))
            throw new HospitalManagementException("Provided password is incorrect");
        doctor.getLoginUser().setPassword(passwordEncoder.encode(password.getNewPassword()));
        doctorRepo.save(doctor);
    }

    @Override
    public List<DoctorDTO> getDoctorsBySearch(String keyword) {
        return doctorRepo.searchDoctorsByKeyword(keyword).stream().map(this::getDoctorProfile).toList();
    }

    @Override
    public List<DoctorDTO> getDoctorsByDepartment(String department) {
        return doctorRepo.findAllByDepartment(department).stream().map(this::getDoctorProfile).toList();
    }

    @Override
    public List<DoctorDTO> getHospitalDoctors(Hospital hospital) {
        List<Doctor> doctors = doctorRepo.getDoctorByHospital(hospital);
        return doctors.stream().map(this::getDoctorProfile).toList();
    }

    @Override
    public void registerDoctor(RegistrationDTO doctor) {
        authService.createDoctor(doctor);
    }

    @Override
    @Transactional
    public void saveFromExcel(MultipartFile file, long hospitalId) throws HospitalManagementException {
        try (InputStream is = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            if (rows.hasNext()) {
                Row headerRow = rows.next();
                verifyHeaders(headerRow);
            }
            while (rows.hasNext()) {
                Row row = rows.next();
                if(userRepo.findByUsername(row.getCell(0).getStringCellValue()).isPresent()) continue;
                RegistrationDTO registrationDTO = createRegistrationDTO(row, hospitalId);
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
            registrationDTO.setMobile(Long.parseLong(getCellValue(row, 6)));
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
                case NUMERIC-> this.getNumericalCellValue(cell);
                case STRING -> cell.getStringCellValue().trim();
                default -> throw new HospitalManagementException("Your Excel File structure is incorrect.");
        };
    }

    private String getNumericalCellValue(Cell cell) {
        return BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString();
    }

    private Gender parseGender(String genderString) {
        if (genderString != null && !genderString.isEmpty()) {
            try {
                return Gender.valueOf(genderString.toUpperCase());
            } catch (Exception _) {}
        }
        return Gender.OTHER;
    }

    private void verifyHeaders(Row headerRow) {
        String[] expectedHeaders = {
                "Username", "Password", "First Name", "Last Name",
                "Gender", "Email", "Mobile", "Department",
                "Specialty", "License Number"
        };
        for (int i = 0; i < expectedHeaders.length; i++) {
            Cell headerCell = headerRow.getCell(i);
            if (headerCell == null || !expectedHeaders[i].equalsIgnoreCase(headerCell.getStringCellValue().trim())) {
                throw new HospitalManagementException("Invalid Excel file structure. Expected header: " + expectedHeaders[i]);
            }
        }
    }

}
