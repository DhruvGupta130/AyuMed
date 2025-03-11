package com.example.system.dto;

import com.example.system.entity.Patient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class RecordsDTO {

    private Long id;
    private Patient patient;
    private String fileName;
    private String filePath;
    private Date uploadDate;
    private String fileType;
    private String description;

}
