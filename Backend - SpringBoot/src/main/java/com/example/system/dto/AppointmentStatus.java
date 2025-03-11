package com.example.system.dto;

public enum AppointmentStatus {
    BOOKED,
    APPROVED,
    COMPLETED,
    CANCELED,
    EXPIRED;

    public boolean canTransitionTo(AppointmentStatus newStatus) {
        return switch (this) {
            case BOOKED -> newStatus == APPROVED || newStatus == CANCELED;
            case APPROVED -> newStatus == COMPLETED || newStatus == CANCELED;
            default -> false;
        };
    }
}
