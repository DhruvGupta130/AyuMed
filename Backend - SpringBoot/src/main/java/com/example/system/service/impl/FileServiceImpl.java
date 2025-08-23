package com.example.system.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.system.entity.File;
import com.example.system.exception.HospitalManagementException;
import com.example.system.repository.FileRepo;
import com.example.system.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepo fileRepo;
    private final Cloudinary cloudinary;

    @Override
    public File saveFile(MultipartFile file) throws IOException {
        var uploadResult = cloudinary.uploader().upload(file.getBytes(),
                Map.of("resource_type", "auto"));
        File newFile = new File();
        newFile.setFileName(file.getOriginalFilename());
        newFile.setFileType(file.getContentType());
        newFile.setPublicId((String) uploadResult.get("public_id"));
        newFile.setFileUrl((String) uploadResult.get("secure_url"));
        newFile.setFileSize(file.getSize());
        return fileRepo.save(newFile);
    }

    @Override
    public String deleteFile(String fileName) throws IOException {
        File file = fileRepo.findByFileName(fileName)
                .orElseThrow(() -> new HospitalManagementException("File not found."));
        cloudinary.uploader().destroy(file.getPublicId(), ObjectUtils.emptyMap());
        fileRepo.delete(file);
        return "File " + fileName + " deleted successfully from Cloudinary.";
    }

    @Override
    public String downloadFile(String fileName) {
        File file = fileRepo.findByFileName(fileName)
                .orElseThrow(() -> new HospitalManagementException("File not found."));
        return file.getFileUrl();
    }

}
