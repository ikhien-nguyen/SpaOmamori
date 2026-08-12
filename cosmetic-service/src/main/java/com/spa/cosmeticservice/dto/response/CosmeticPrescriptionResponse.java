package com.spa.cosmeticservice.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CosmeticPrescriptionResponse {
    private String id;
    private String appointmentId;
    private String technicianId;
    private String cosmeticId;
    private String cosmeticName;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private LocalDateTime prescribedAt;
}
