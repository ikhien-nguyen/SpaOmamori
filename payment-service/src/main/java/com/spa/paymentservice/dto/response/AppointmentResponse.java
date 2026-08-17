package com.spa.paymentservice.dto.response;

import lombok.*;

import java.math.BigDecimal;

/**
 * Mirror rut gon cua AppointmentResponse ben appointment-service - chi khai
 * bao field payment-service thuc su can dung (dung convention "moi service
 * tu dinh nghia lai DTO cua minh", muc 1 style guide).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentResponse {
    private String id;
    private String customerId;
    private String status;
    private String serviceId;
    private String serviceName;
    private BigDecimal servicePrice;
    private String roomId;
    private String roomName;
    private BigDecimal roomPrice;
}
