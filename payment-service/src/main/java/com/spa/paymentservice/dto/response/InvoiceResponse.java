package com.spa.paymentservice.dto.response;

import com.spa.paymentservice.entity.InvoiceStatus;
import com.spa.paymentservice.entity.PaymentMethod;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceResponse {
    private String id;
    private String customerId;
    private String appointmentId;
    private InvoiceStatus status;
    private PaymentMethod paymentMethod;
    private BigDecimal totalAmount;
    private List<InvoiceItemResponse> items;
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;
}
