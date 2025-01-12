package com.example.system.controller;

import com.example.system.exception.HospitalManagementException;
import com.example.system.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@RestController
@AllArgsConstructor
public class FileController {

    private final FileService fileService;


    @PostMapping("/upload-image")
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            return fileService.saveFile(file).getFilePath();
        } catch (IOException e) {
            throw new HospitalManagementException(e.getMessage());
        }
    }

    @DeleteMapping("/delete-image")
    public String deleteImage(@RequestBody Map<String, String> data) {
        String fileName = data.get("fileName");
        try {
            return fileService.deleteFile(fileName);
        } catch (IOException e) {
            throw new HospitalManagementException("Failed to delete the image: " + e.getMessage());
        }
    }

    @GetMapping("/get/profile")
    public ResponseEntity<Resource> downloadFile(@RequestParam("image") String filePath) {
        Path path = Path.of(filePath);
        Resource resource = new FileSystemResource(path);

        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.notFound().build();
        }

        String contentType;
        try {
            contentType = Files.probeContentType(path);
        } catch (IOException e) {
            throw new HospitalManagementException(e.getMessage());
        }
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


}
