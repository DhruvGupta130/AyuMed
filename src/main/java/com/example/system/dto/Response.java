package com.example.system.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class Response {

    private String message;
    private String error;
    private HttpStatus status;
}
