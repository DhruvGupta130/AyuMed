package com.example.system.controller;

import com.example.system.exception.HospitalManagementException;
import com.example.system.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@AllArgsConstructor
public class FileController {

    private final FileService fileService;


    @PostMapping("/upload-image")
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            return fileService.saveFile(file).getFileUrl();
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

}
