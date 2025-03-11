package com.example.system.service;

import com.example.system.entity.File;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    File saveFile(MultipartFile file) throws IOException;
    String deleteFile(String fileName) throws IOException;
    String downloadFile(String filePath);
}
