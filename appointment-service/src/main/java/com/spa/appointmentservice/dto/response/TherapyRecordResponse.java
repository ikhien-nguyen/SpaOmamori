package com.spa.appointmentservice.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TherapyRecordResponse {
    private String id;
    private String appointmentId;
    private String conditionNotes;
    private String improvementNotes;
    private Integer remainingSessions;
    private LocalDateTime recordedAt;
}