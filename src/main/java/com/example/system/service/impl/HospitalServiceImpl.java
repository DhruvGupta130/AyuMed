package com.example.system.service.impl;

import com.example.system.dto.HospitalDTO;
import com.example.system.entity.Address;
import com.example.system.entity.Hospital;
import com.example.system.entity.Manager;
import com.example.system.exception.HospitalManagementException;
import com.example.system.repository.AddressRepo;
import com.example.system.repository.HospitalRepo;
import com.example.system.repository.ManagerRepo;
import com.example.system.service.HospitalService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class HospitalServiceImpl implements HospitalService {

    private final HospitalRepo hospitalRepo;
    private final ManagerRepo managerRepo;
    private final AddressRepo addressRepo;

    @Override
    @Transactional
    public void registerHospital(HospitalDTO hospitalDTO, Manager manager) {
        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalDTO, hospital);
        addressRepo.save(hospital.getAddress());
        hospital.setManager(manager);
        hospitalRepo.save(hospital);
        manager.setHospital(hospital);
        managerRepo.save(manager);
    }

    @Override
    public HospitalDTO getHospitalProfile(Manager manager) {
        HospitalDTO hospitalDTO = new HospitalDTO();
        BeanUtils.copyProperties(manager.getHospital(), hospitalDTO);
        return hospitalDTO;
    }

    @Override
    public List<HospitalDTO> getHospitalsWithinRadius(double latitude, double longitude, double radius) {
        List<Hospital> hospitals = hospitalRepo.findHospitalsWithinRadius(latitude, longitude, radius);
        return hospitals.stream()
                .map(hospital -> new HospitalDTO(hospital.getHospitalName(),
                        hospital.getAddress(), hospital.getMobile(),
                        hospital.getEmail(), hospital.getWebsite(),
                        hospital.getEstablishedYear(), hospital.getDescription(),
                        hospital.getImages())
                ).toList();
    }

    @Override
    public List<HospitalDTO> searchHospital(String keyword) {
        List<Hospital> hospitals = hospitalRepo.searchByKeyword(keyword);
        return hospitals.stream().map(hospital ->
                new HospitalDTO(hospital.getHospitalName(), hospital.getAddress(),
                        hospital.getMobile(), hospital.getEmail(), hospital.getWebsite(),
                        hospital.getEstablishedYear(), hospital.getDescription(),
                        hospital.getImages())
        ).toList();
    }

    @Override
    public void updateAddress(Manager manager, Address address) {
        Address address1 = addressRepo.findById(manager.getHospital().getAddress().getId()).
                orElseThrow(()-> new HospitalManagementException("Address not found"));
        address.setId(address1.getId());
        addressRepo.save(address);
    }
}
