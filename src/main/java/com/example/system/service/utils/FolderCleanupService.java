package com.example.system.service.utils;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

@Service
public class FolderCleanupService {

    @Value("${file.storage.path}")
    private String storagePath;

    @Scheduled(cron = "0 0 0 * * *")
    public void cleanEmptyFolders() {
        try {
            Files.walkFileTree(Paths.get(storagePath).getParent(), new SimpleFileVisitor<Path>() {
                @Override
                @NonNull
                public FileVisitResult visitFile(Path file, @NonNull  BasicFileAttributes attrs) {
                    return FileVisitResult.CONTINUE;
                }
                @Override
                @NonNull
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    if (isDirectoryEmpty(dir)) {
                        Files.delete(dir);
                        System.out.println("Deleted empty folder: " + dir);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            System.err.println("Error during folder cleanup: " + e.getMessage());
        }
    }

    private boolean isDirectoryEmpty(Path dir) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            return !stream.iterator().hasNext(); // Check if directory is empty
        }
    }
}