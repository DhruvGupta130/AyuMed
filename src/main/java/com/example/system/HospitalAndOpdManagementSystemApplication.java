package com.example.system;

import com.example.system.service.utils.FolderCleanupService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HospitalAndOpdManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(HospitalAndOpdManagementSystemApplication.class, args);
    }

}
