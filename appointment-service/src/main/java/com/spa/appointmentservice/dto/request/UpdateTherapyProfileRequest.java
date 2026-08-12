package com.spa.appointmentservice.dto.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTherapyProfileRequest {
    private String initialSkinCondition;
    private String medicalHistory;
}
