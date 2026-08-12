package com.spa.appointmentservice.dto.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTherapyProfileRequest {
    private String initialSkinCondition;
    private String medicalHistory;
}
