package com.spa.appointmentservice.dto.response;

import lombok.*;

import java.math.BigDecimal;

/**
 * Khớp shape với TreatmentResponse bên treatment-service trả về —
 * chỉ giữ field appointment-service thực sự cần dùng.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TreatmentResponse {
    private String id;
    private String name;
    private BigDecimal price;
    private Integer durationMinutes;
    private Boolean isActive;
}
