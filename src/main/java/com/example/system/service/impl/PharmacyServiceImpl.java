package com.example.system.service.impl;

import com.example.system.dto.*;
import com.example.system.entity.*;
import com.example.system.exception.HospitalManagementException;
import com.example.system.repository.*;
import com.example.system.service.PatientService;
import com.example.system.service.PharmacyService;
import com.example.system.service.utils.EmailService;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
@AllArgsConstructor
public class PharmacyServiceImpl implements PharmacyService {

    private final PharmacyRepo pharmacyRepo;
    private final PharmacistRepo pharmacistRepo;
    private final AddressRepo addressRepo;
    private final MedicationRepo medicationRepo;
    private final PasswordEncoder passwordEncoder;
    private final PatientService patientService;
    private final PatientRepo patientRepo;
    private final EmailStructures emailStructures;
    private final EmailService emailService;

    @Override
    @Transactional
    public void createPharmacy(Pharmacy pharmacy, Pharmacist pharmacist) {
        try {
            pharmacy.setPharmacist(pharmacist);
            pharmacist.setPharmacy(pharmacy);
            pharmacyRepo.save(pharmacy);
            String body = emailStructures.generatePharmacyWelcomeEmail(pharmacy.getPharmacyName());
            emailService.sendEmail(
                    pharmacist.getLoginUser().getEmail(),
                    "\uD83D\uDC8A Welcome to AyuMed Pharmacy Network! Let’s Get Started",
                    body
            );
        } catch (Exception e) {
            throw new HospitalManagementException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void updatePharmacy(PharmacyDTO pharmacyDTO, Pharmacist pharmacist) {
        Pharmacy pharmacy = pharmacyRepo.findById(pharmacist.getPharmacy().getId())
                .orElseThrow(() -> new HospitalManagementException("Pharmacy not found"));
        if(pharmacyDTO.getPharmacyName()!=null) pharmacy.setPharmacyName(pharmacyDTO.getPharmacyName());
        if(pharmacyDTO.getEmail()!=null) pharmacy.setEmail(pharmacyDTO.getEmail());
        if(pharmacyDTO.getMobile()!=null) pharmacy.setMobile(pharmacyDTO.getMobile());
        if(pharmacyDTO.getClosingTime()!=null) pharmacy.setClosingTime(pharmacyDTO.getClosingTime());
        if(pharmacyDTO.getOpeningTime()!=null) pharmacy.setOpeningTime(pharmacyDTO.getOpeningTime());
        if(pharmacyDTO.getImages()!=null) pharmacy.setImages(pharmacyDTO.getImages());
        pharmacy.setOpen(true);
        pharmacyRepo.save(pharmacy);
    }

    @Override
    public PharmacyDTO getPharmacyProfile(Pharmacy pharmacy) {
        if(pharmacy == null) throw new HospitalManagementException("Pharmacy not found");
        return new PharmacyDTO(
                pharmacy.getId(), pharmacy.getPharmacyName(),
                pharmacy.getOverview(), pharmacy.getServices(),
                pharmacy.getPharmacyTechnology(), pharmacy.getAccreditations(),
                pharmacy.getInsurancePartners(),
                pharmacy.getAddress(), pharmacy.getEmail(),
                pharmacy.getMobile(), pharmacy.getWebsite(),
                pharmacy.isOpen(), pharmacy.getOpeningTime(),
                pharmacy.getClosingTime(), pharmacy.getImages()
        );
    }

    @Override
    public PharmacistDTO getPharmacistProfile(Pharmacist pharmacist) {
        return new PharmacistDTO(
                pharmacist.getId(), pharmacist.getFirstName(),
                pharmacist.getLastName(), pharmacist.getGender(),
                pharmacist.getLoginUser().getEmail(), pharmacist.getMobile(),
                pharmacist.getFirstName() + " " + pharmacist.getLastName()
        );
    }

    @Override
    public void updatePharmacy(Medication medication, Pharmacist pharmacist) {
        if(pharmacist.getPharmacy()==null) throw new HospitalManagementException("Pharmacy not found");
        if(medication.isExpired()) throw new HospitalManagementException("Medication is expired");
        Optional<Medication> optional = medicationRepo.findExistingMedication(medication.getBatchNumber(), medication.getMedicationName(), medication.getExpiry());
        if(optional.isPresent()) {
            updateMedication(optional.get(), medication.getQuantity());
        } else {
            medication.setPharmacy(pharmacist.getPharmacy());
            pharmacist.getPharmacy().getMedications().add(medication);
            medicationRepo.save(medication);
        }
    }

    @Override
    @Transactional
    public void saveFromExcel(MultipartFile file, Pharmacist pharmacist) throws HospitalManagementException {
        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            if (rows.hasNext()) rows.next();
            while (rows.hasNext()) {
                Row row = rows.next();
                LocalDate expiryDate = row.getCell(5).getLocalDateTimeCellValue().toLocalDate();
                if(expiryDate.isBefore(LocalDate.now())) continue;
                String batchNumber = row.getCell(8).getStringCellValue().trim();
                String medicationName = row.getCell(0).getStringCellValue().trim();
                Optional<Medication> existingMedication = medicationRepo.findExistingMedication(batchNumber, medicationName, expiryDate);
                if (existingMedication.isPresent()) {
                    updateMedication(existingMedication.get(), (long) row.getCell(4).getNumericCellValue());
                } else {
                    Medication medication = createMedication(row);
                    updatePharmacy(medication, pharmacist);
                }
            }
        } catch (IOException e) {
            throw new HospitalManagementException("Error reading the Excel file: " + e.getMessage(), e);
        }
    }
    private Medication createMedication(Row row) {
        try {
            Medication medication = new Medication();
            medication.setMedicationName(row.getCell(0).getStringCellValue());
            medication.setCompositionName(row.getCell(1).getStringCellValue());
            medication.setDosageForm(row.getCell(2).getStringCellValue());
            medication.setStrength(row.getCell(3).getStringCellValue());
            medication.setQuantity((int) row.getCell(4).getNumericCellValue());
            medication.setExpiry(row.getCell(5).getLocalDateTimeCellValue().toLocalDate());
            medication.setManufacturer(row.getCell(6).getStringCellValue());
            medication.setPrice(row.getCell(7).getNumericCellValue());
            medication.setBatchNumber(row.getCell(8).getStringCellValue());
            return medication;
        } catch (Exception e) {
            throw new HospitalManagementException("Error creating Medication: " + e.getMessage(), e);
        }
    }
    private void updateMedication(Medication medication, long quantity) {
        try {
            medication.setQuantity(medication.getQuantity() + quantity);
            medicationRepo.save(medication);
        } catch (Exception e) {
            throw new HospitalManagementException("Error updating medication: " + e.getMessage(), e);
        }
    }

    @Override
    public void setStatus(Pharmacy pharmacy) {
        if(pharmacy.getClosingTime().isBefore(LocalTime.now())
                || pharmacy.getOpeningTime().isAfter(LocalTime.now())) {
            throw new HospitalManagementException("Outside working hours: "+ pharmacy.getClosingTime());
        }else pharmacy.setOpen(!pharmacy.isOpen());
        pharmacyRepo.save(pharmacy);
    }

    @Override
    public void updatePassword(Pharmacist pharmacist, Password password) {
        if(!passwordEncoder.matches(password.getOldPassword(), pharmacist.getLoginUser().getPassword()))
            throw new HospitalManagementException("Provided password is incorrect");
        this.updatePassword(pharmacist, password.getNewPassword());
    }

    @Override
    public void updatePassword(Pharmacist pharmacist, String password) {
        pharmacist.getLoginUser().setPassword(passwordEncoder.encode(password));
        pharmacistRepo.save(pharmacist);
    }

    @Override
    public void updateAddress(Pharmacist pharmacist, Address address) {
        Address address1 = addressRepo.findById(pharmacist.getPharmacy().getAddress().getId())
                .orElseThrow(() -> new HospitalManagementException("Address not found"));
        address.setId(address1.getId());
        addressRepo.save(address);
    }

    private Medication getMedication(long id) {
        return medicationRepo.findById(id)
                .orElseThrow(() -> new HospitalManagementException("Medication not found"));
    }

    @Override
    public MedicationDTO getMedicationById(long id) {
        return this.getMedicationDTO(getMedication(id));
    }

    @Scheduled(fixedRate = 1800000)
    public void updatePharmacyStatus() {
        List<Pharmacy> pharmacies = pharmacyRepo.findAll();
        for (Pharmacy pharmacy : pharmacies) {
            this.updateStatus(pharmacy);
        }
    }

    private void updateStatus(Pharmacy pharmacy) {
        if(pharmacy.getClosingTime().isBefore(LocalTime.now())
                || pharmacy.getOpeningTime().isAfter(LocalTime.now())) {
            pharmacy.setOpen(false);
            pharmacyRepo.save(pharmacy);
        }
    }

    @Override
    public List<MedicationDTO> getMedications() {
        return medicationRepo.findAll().stream().filter(medication -> medication.getPatients().isEmpty()).map(this::getMedicationDTO).toList();
    }

    @Override
    public List<MedicationDTO> getMedicationsByPharmacy(long pharmacyId) {
        Pharmacy pharmacy = pharmacyRepo.findById(pharmacyId)
                .orElseThrow(() -> new HospitalManagementException("Pharmacy not found"));
        return this.getMedicationsByPharmacy(pharmacy);
    }

    @Override
    public List<MedicationDTO> getMedicationsByPharmacy(Pharmacy pharmacy) {
        return pharmacy.getMedications().stream().filter(m -> m.getPatients().isEmpty()).map(this::getMedicationDTO).toList();
    }

    @Override
    public List<PharmacyPatientsDTO> getPharmacyPatients(Pharmacy pharmacy) {
        return patientService.getPharmacyPatient(pharmacy);
    }

    @Override
    public List<PharmacyDTO> getPharmaciesByKeyword(String keyword) {
        return pharmacyRepo.findPharmaciesByKeyword(keyword).stream()
                .map(this::getPharmacyProfile).toList();
    }

    @Override
    public List<PharmacyDTO> getPharmaciesWithinRadius(double latitude, double longitude, double radius) {
        List<Pharmacy> pharmacies = medicationRepo.findPharmacyWithinRadius(latitude, longitude, radius);
        return pharmacies.stream().map(this::getPharmacyProfile).toList();
    }

    @Override
    public PharmacyDTO getPharmacyById(long id) {
        return getPharmacyProfile(pharmacyRepo.getPharmacyById(id));
    }

    @Override
    public List<MedicationDTO> getMedicationsByKeyword(String keyword) {
        return medicationRepo.findByKeyword(keyword).stream().map(this::getMedicationDTO).toList();
    }

    @Override
    public List<MedicationDTO> getPatientMedications(Patient patient) {
        return medicationRepo.getMedicationsByPatient(patient).stream().map(this::getMedicationDTO).toList();
    }

    @Override
    @Transactional
    public void buyMedications(Patient patient, Map<Long, Long> medicationOrders) {
        List<Medication> validMedications = medicationRepo.findAllById(medicationOrders.keySet()).stream()
                .filter(medication ->
                        medication.getQuantity() >= medicationOrders.getOrDefault(medication.getId(), 0L)
                        && medication.getPatients().isEmpty())
                .peek(medication ->
                        medication.setQuantity(medication.getQuantity() - medicationOrders.get(medication.getId())))
                .toList();
        medicationRepo.saveAll(validMedications);
        List<Medication> patientMedications = validMedications.stream()
                .map(medication -> getMedication(medicationOrders.get(medication.getId()), medication))
                .toList();
        double totalAmount = patientMedications.stream()
                .mapToDouble(Medication::getPrice)
                .sum();

        patient.getMedications().addAll(patientMedications);
        patientRepo.save(patient);
        String s = emailStructures.generateMedicationBillEmail(patient.getFullName(), patientMedications, totalAmount);
        emailService.sendEmail(patient.getLoginUser().getEmail(), "Medication Bill", s);
    }

    private Medication getMedication(long quantity, Medication medication) {
        Medication patientMedication = new Medication();
        patientMedication.setMedicationName(medication.getMedicationName());
        patientMedication.setCompositionName(medication.getCompositionName());
        patientMedication.setDosageForm(medication.getDosageForm());
        patientMedication.setStrength(medication.getStrength());
        patientMedication.setPharmacy(medication.getPharmacy());
        patientMedication.setQuantity(quantity);
        patientMedication.setPrice(medication.getPrice() * quantity);
        patientMedication.setExpiry(medication.getExpiry());
        patientMedication.setManufacturer(medication.getManufacturer());
        patientMedication.setBatchNumber(medication.getBatchNumber());
        return patientMedication;
    }

    private MedicationDTO getMedicationDTO(Medication medication) {
        return new MedicationDTO(
                medication.getId(), medication.getMedicationName(),
                medication.getCompositionName(), medication.getDosageForm(),
                medication.getStrength(), medication.getQuantity(),
                medication.getExpiry(), medication.getExpiry().isBefore(LocalDate.now()),
                medication.getManufacturer(), medication.getPrice(),
                medication.getBatchNumber(), medication.getPharmacy().getPharmacyName()
        );
    }
}