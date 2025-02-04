package com.example.system.service;

import com.example.system.dto.*;
import com.example.system.entity.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public interface PharmacyService {

    void createPharmacy(Pharmacy pharmacy, Pharmacist pharmacist);
    void updatePharmacy(PharmacyDTO pharmacy, Pharmacist pharmacist);
    PharmacyDTO getPharmacyProfile(Pharmacy pharmacy);
    PharmacistDTO getPharmacistProfile(Pharmacist pharmacist);
    void updatePharmacy(Medication medication, Pharmacist pharmacist);
    void saveFromExcel(MultipartFile file, Pharmacist pharmacist);
    void setStatus(Pharmacy pharmacy);
    void updatePassword(Pharmacist pharmacist, Password password);
    void updatePassword(Pharmacist pharmacist, String password);
    void updateAddress(Pharmacist pharmacist, Address address);
    MedicationDTO getMedicationById(long id);
    List<MedicationDTO> getMedications();
    List<MedicationDTO> getMedicationsByPharmacy(long pharmacyId);
    List<MedicationDTO> getMedicationsByPharmacy(Pharmacy pharmacy);
    List<PharmacyPatientsDTO> getPharmacyPatients(Pharmacy pharmacy);
    List<MedicationDTO> getMedicationsByKeyword(String keyword);
    List<MedicationDTO> getPatientMedications(Patient patient);
    void buyMedications(Patient patient, Map<Long, Long> medicationOrders);
    List<PharmacyDTO> getPharmaciesByKeyword(String keyword);
    List<PharmacyDTO> getPharmaciesWithinRadius(double latitude, double longitude, double radius);
    PharmacyDTO getPharmacyById(long id);
}
