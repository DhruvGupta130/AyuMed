package com.example.system.service;

import com.example.system.dto.DoctorDTO;
import com.example.system.dto.HospitalDTO;
import com.example.system.dto.ManagerDTO;
import com.example.system.dto.Password;
import com.example.system.entity.Address;
import com.example.system.entity.Hospital;
import com.example.system.entity.Manager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HospitalService {

    void registerHospital(Hospital hospital, Manager manager);
    HospitalDTO getHospitalProfile(Hospital hospital);
    void updatePassword(Manager manager, Password password);
    ManagerDTO getManagerProfile(Manager manager);
    List<HospitalDTO> getHospitalsWithinRadius(double latitude, double longitude, double radius);
    List<HospitalDTO> searchHospital(String keyword);
    List<DoctorDTO> getAllDoctors(Hospital hospital);
    void updateAddress(Manager manager, Address address);
}
