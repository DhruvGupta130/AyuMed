package com.example.system.service;

import com.example.system.dto.Password;
import com.example.system.dto.PharmacistDTO;
import com.example.system.dto.PharmacyDTO;
import com.example.system.entity.Address;
import com.example.system.entity.Medication;
import com.example.system.entity.Pharmacist;
import com.example.system.entity.Pharmacy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface PharmacyService {

    void createPharmacy(PharmacyDTO pharmacy, Pharmacist pharmacist);
    void updatePharmacy(PharmacyDTO pharmacy, Pharmacist pharmacist);
    PharmacyDTO getPharmacyProfile(Pharmacy pharmacy);
    PharmacistDTO getPharmacistProfile(Pharmacist pharmacist);
    void updatePharmacy(Medication medication, Pharmacist pharmacist);
    void saveFromExcel(MultipartFile file, Pharmacist pharmacist);
    void setStatus(Pharmacy pharmacy);
    void updatePassword(Pharmacist pharmacist, Password password);
    void updateAddress(Pharmacist pharmacist, Address address);
    List<Medication> getMedications();
    List<Medication> getMedicationsByKeyword(String keyword);
    PharmacyDTO getPharmacy(Pharmacist pharmacist);
    List<PharmacyDTO> getPharmaciesWithinRadius(double latitude, double longitude, double radius);
}
