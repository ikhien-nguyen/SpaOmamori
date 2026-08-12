package com.spa.appointmentservice.entity;

/**
 * Luong trang thai: PENDING -> CONFIRMED -> IN_PROGRESS -> COMPLETED
 *                                        \-> CANCELLED (co the huy tu PENDING/CONFIRMED)
 */
public enum AppointmentStatus {
    PENDING,        // Cho Admin xac nhan
    CONFIRMED,      // Admin da duyet, cho den ngay hen
    IN_PROGRESS,
    COMPLETED,
    CANCELLED,
}
