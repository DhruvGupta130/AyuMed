package com.example.system.service.impl;

import com.example.system.entity.File;
import com.example.system.exception.HospitalManagementException;
import com.example.system.repository.FileRepo;
import com.example.system.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileServiceImpl implements FileService {

    private final FileRepo fileRepo;

    @Value("${file.storage.path}")
    private String storagePath;

    public FileServiceImpl(FileRepo fileRepo) {
        this.fileRepo = fileRepo;
    }

    @Override
    public File saveFile(MultipartFile file) throws IOException {
        File newFile = new File();
        String patientDirPath = storagePath + "/";
        String filePath = patientDirPath + "/" + file.getOriginalFilename();
        Path parentDir = Paths.get(patientDirPath);
        Path dest = Paths.get(filePath);
        if(Files.notExists(parentDir)) {
            Files.createDirectories(parentDir);
        }
        file.transferTo(dest);
        newFile.setFileName(file.getOriginalFilename());
        newFile.setFileType(file.getContentType());
        newFile.setFilePath(filePath);
        newFile.setFileSize(file.getSize());
        return fileRepo.save(newFile);
    }

    @Override
    public String deleteFile(String fileName) throws IOException {
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("Invalid file name provided.");
        }
        File file = fileRepo.findByFileName(fileName)
                .orElseThrow(() -> new HospitalManagementException("File not found."));
        String filePath = storagePath + "/" + fileName;
        Path fileToDelete = Paths.get(filePath);

        if (Files.exists(fileToDelete)) {
            Files.delete(fileToDelete);
            fileRepo.delete(file);
            return "File " + fileName + " deleted successfully.";
        } else {
            throw new IOException("File " + fileName + " does not exist.");
        }
    }

    @Override
    public String downloadFile(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("Invalid file name provided.");
        }

        String filePath = storagePath + "/" + fileName;
        Path fileToDownload = Paths.get(filePath);

        if (Files.exists(fileToDownload)) {
            return filePath;
        } else {
            throw new RuntimeException("File " + fileName + " does not exist.");
        }
    }

}
