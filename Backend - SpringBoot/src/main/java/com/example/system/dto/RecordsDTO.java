package com.example.system.dto;

import com.example.system.entity.Patient;
import com.example.system.entity.PatientRecord;

import java.util.Date;

public record RecordsDTO(
        Long id,
        Patient patient,
        String fileName,
        String fileUrl,
        Date uploadDate,
        String fileType,
        String description
) {
    public RecordsDTO(PatientRecord patientRecord) {
        this(
                patientRecord.getId(),
                patientRecord.getPatient(),
                patientRecord.getFileName(),
                patientRecord.getFileUrl(),
                patientRecord.getUploadDate(),
                patientRecord.getFileType(),
                patientRecord.getDescription()
        );
    }
}