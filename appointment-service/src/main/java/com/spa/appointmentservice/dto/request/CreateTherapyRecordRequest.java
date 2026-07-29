package com.spa.appointmentservice.dto.request;

import lombok.*;

/**
 * Dữ liệu cho use case "Cập nhật kết quả trị liệu" (Therapist, khi hoàn thành lịch hẹn).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTherapyRecordRequest {
    private String conditionNotes;
    private String improvementNotes;
    private Integer remainingSessions;
}