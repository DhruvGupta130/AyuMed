package com.example.system.exception;

public class HospitalManagementException extends RuntimeException {
    public HospitalManagementException(String message) {
        super(message);
    }
    public HospitalManagementException(String message, Throwable cause) {
        super(message, cause);
    }
}

