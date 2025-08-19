package com.example.system.service.impl;

import com.cloudinary.Cloudinary;
import com.example.system.dto.RecordsDTO;
import com.example.system.entity.Patient;
import com.example.system.entity.PatientRecord;
import com.example.system.repository.PatientRecordRepo;
import com.example.system.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService {

    private final PatientRecordRepo patientRecordRepo;
    private final Cloudinary cloudinary;

    @Override
    @Transactional
    public void uploadFile(Patient patient, MultipartFile file, String description) throws IOException {
        var uploadResult = cloudinary.uploader().upload(file.getBytes(),
                Map.of("resource_type", "auto"));
        PatientRecord record = new PatientRecord();
        record.setPatient(patient);
        record.setFileName((String) uploadResult.get("original_filename"));
        record.setPublicId((String) uploadResult.get("public_id"));
        record.setFileUrl((String) uploadResult.get("secure_url"));
        record.setFileType(file.getContentType());
        record.setDescription(description);
        patientRecordRepo.save(record);
    }

    @Override
    public Resource downloadFile(Long fileId) throws IOException {
        PatientRecord record = patientRecordRepo.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("File not found"));
        try {
            return new UrlResource(record.getFileUrl());
        } catch (MalformedURLException e) {
            throw new IOException("Invalid file URL", e);
        }
    }

    @Override
    @Transactional
    public void deleteFile(Long fileId) throws IOException {
        PatientRecord record = patientRecordRepo.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("File not found"));
        cloudinary.uploader().destroy(record.getPublicId(), Map.of());
        patientRecordRepo.delete(record);
    }

    @Override
    public List<RecordsDTO> getFilesByPatientId(Patient patient) {
        return patientRecordRepo.findByPatient(patient)
                .stream().map(RecordsDTO::new).toList();
    }
}
