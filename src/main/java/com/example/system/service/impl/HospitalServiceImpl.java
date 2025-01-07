package com.example.system.service.impl;

import com.example.system.dto.DoctorDTO;
import com.example.system.dto.HospitalDTO;
import com.example.system.dto.ManagerDTO;
import com.example.system.dto.Password;
import com.example.system.entity.Address;
import com.example.system.entity.Hospital;
import com.example.system.entity.Manager;
import com.example.system.exception.HospitalManagementException;
import com.example.system.repository.AddressRepo;
import com.example.system.repository.HospitalRepo;
import com.example.system.repository.ManagerRepo;
import com.example.system.service.DoctorService;
import com.example.system.service.HospitalService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class HospitalServiceImpl implements HospitalService {

    private final HospitalRepo hospitalRepo;
    private final ManagerRepo managerRepo;
    private final AddressRepo addressRepo;
    private final PasswordEncoder passwordEncoder;
    private final DoctorService doctorService;

    @Override
    @Transactional
    public void registerHospital(Hospital hospital, Manager manager) {
        addressRepo.save(hospital.getAddress());
        hospital.setManager(manager);
        hospitalRepo.save(hospital);
        manager.setHospital(hospital);
        managerRepo.save(manager);
    }

    @Override
    public HospitalDTO getHospitalProfile(Hospital hospital) {
        return new HospitalDTO(
                hospital.getId(), hospital.getHospitalName(),
                hospital.getAddress(), hospital.getEmail(),
                hospital.getMobile(), hospital.getDepartments(),
                hospital.getWebsite(), hospital.getEstablishedYear(),
                hospital.getDescription(), hospital.getImages()
        );
    }

    @Override
    public void updatePassword(Manager manager, Password password) {
        if(!passwordEncoder.matches(password.getOldPassword(), manager.getLoginUser().getPassword()))
            throw new HospitalManagementException("Provided password is incorrect");
        manager.getLoginUser().setPassword(passwordEncoder.encode(password.getNewPassword()));
        managerRepo.save(manager);
    }

    @Override
    public ManagerDTO getManagerProfile(Manager manager) {
        return new ManagerDTO(
                manager.getId(), manager.getFirstName(),
                manager.getLastName(), manager.getGender(),
                manager.getEmail(), manager.getMobile(),
                manager.getFullName()
        );
    }

    @Override
    public List<HospitalDTO> getHospitalsWithinRadius(double latitude, double longitude, double radius) {
        List<Hospital> hospitals = hospitalRepo.findHospitalsWithinRadius(latitude, longitude, radius);
        return hospitals.stream().map(this::getHospitalProfile).toList();
    }

    @Override
    public List<HospitalDTO> searchHospital(String keyword) {
        List<Hospital> hospitals = hospitalRepo.searchByKeyword(keyword);
        return hospitals.stream().map(this::getHospitalProfile).toList();
    }

    @Override
    public List<DoctorDTO> getAllDoctors(Hospital hospital) {
        return doctorService.getHospitalDoctors(hospital);
    }

    @Override
    public void updateAddress(Manager manager, Address address) {
        Address address1 = addressRepo.findById(manager.getHospital().getAddress().getId()).
                orElseThrow(()-> new HospitalManagementException("Address not found"));
        address.setId(address1.getId());
        addressRepo.save(address);
    }
}
