package com.spa.appointmentservice.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TherapyProfileResponse {
    private String id;
    private String customerId;
    private String initialSkinCondition;
    private String medicalHistory;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
