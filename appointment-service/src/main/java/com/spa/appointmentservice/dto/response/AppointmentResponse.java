package com.spa.appointmentservice.dto.response;

import com.spa.appointmentservice.entity.AppointmentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentResponse {
    private String id;
    private String customerId;
    private String therapistId;

    private String serviceId;
    private String serviceName;
    private BigDecimal servicePrice;

    private String roomId;
    private String roomName;
    private BigDecimal roomPrice;

    private BigDecimal totalAmount;

    private LocalDateTime appointmentTime;
    private String reason;
    private String note;
    private AppointmentStatus status;
    private LocalDateTime createdAt;
}
