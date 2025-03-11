package com.example.system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class HospitalManagementException extends RuntimeException {
    public HospitalManagementException(String message) {
        super(message);
    }
    public HospitalManagementException(String message, Throwable cause) {
        super(message, cause);
    }
}

