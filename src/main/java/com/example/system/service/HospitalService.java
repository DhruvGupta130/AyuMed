package com.example.system.service;

import com.example.system.dto.HospitalDTO;
import com.example.system.dto.Password;
import com.example.system.entity.Address;
import com.example.system.entity.Manager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HospitalService {

    void registerHospital(HospitalDTO hospitalDTO, Manager manager);
    HospitalDTO getHospitalProfile(Manager manager);
    void updatePassword(Manager manager, Password password);
    List<HospitalDTO> getHospitalsWithinRadius(double latitude, double longitude, double radius);
    List<HospitalDTO> searchHospital(String keyword);
    void updateAddress(Manager manager, Address address);
}
