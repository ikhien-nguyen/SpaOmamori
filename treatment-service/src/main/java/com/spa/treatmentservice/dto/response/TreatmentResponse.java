package com.spa.treatmentservice.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TreatmentResponse {
    private String id;
    private String name;
    private String category;
    private BigDecimal price;
    private Integer durationMinutes;
    private String description;
    private Boolean isActive;
}