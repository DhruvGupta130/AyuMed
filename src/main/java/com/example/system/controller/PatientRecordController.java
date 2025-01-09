package com.example.system.controller;

import com.example.system.dto.RecordsDTO;
import com.example.system.dto.Response;
import com.example.system.entity.Patient;
import com.example.system.exception.HospitalManagementException;
import com.example.system.service.RecordService;
import com.example.system.service.utils.Utility;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/patient")
public class PatientRecordController {

    private final RecordService recordService;
    private final Utility utility;

    @PostMapping("/upload")
    public ResponseEntity<Response> uploadFile(
            @RequestHeader("Authorization") String token,
            @RequestParam("file") MultipartFile file,
            @RequestParam("description") String description) {

        Response response = new Response();
        try {
            Patient patient = (Patient) utility.getUserFromToken(token);
            recordService.uploadFile(patient, file, description);
            response.setMessage("Document successfully uploaded");
            response.setStatus(HttpStatus.ACCEPTED);
        } catch (HospitalManagementException e) {
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);

        } catch (IOException e) {
            response.setMessage("File upload failed: " + e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            response.setMessage("Error while uploading file: " + e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/files")
    public ResponseEntity<List<RecordsDTO>> getFilesByPatient(@RequestHeader("Authorization") String token) {
        Patient patient = (Patient) utility.getUserFromToken(token);
        List<RecordsDTO> records = recordService.getFilesByPatientId(patient);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/{fileId}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {
        Resource file;
        try {
            file = recordService.downloadFile(fileId);
        } catch (IOException e) {
            throw new HospitalManagementException(e.getMessage());
        }

        String contentType = "application/octet-stream";
        try {
            if(file.getFilename()!=null)
                contentType = Files.probeContentType(Paths.get(file.getFilename()));
        } catch (IOException e) {
            throw new HospitalManagementException(e.getMessage());
        }

        if (contentType.startsWith("image/") || contentType.equals("application/pdf")) {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(file);
        }

        String contentDisposition = "attachment; filename=\"" + file.getFilename() + "\"";
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(file);
    }

    @Transactional
    @DeleteMapping("/{fileId}/delete")
    public ResponseEntity<Response> deleteFile(@PathVariable Long fileId) {
        Response response = new Response();
        try {
            recordService.deleteFile(fileId);
            response.setMessage("File deleted successfully");
            response.setStatus(HttpStatus.OK);
        } catch (HospitalManagementException e) {
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            response.setMessage("File delete failed: " + e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            response.setMessage("Error while deleting file: " + e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}

