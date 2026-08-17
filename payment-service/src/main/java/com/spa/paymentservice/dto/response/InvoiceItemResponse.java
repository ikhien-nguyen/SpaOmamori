package com.spa.paymentservice.dto.response;

import com.spa.paymentservice.entity.InvoiceItemType;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceItemResponse {
    private String id;
    private InvoiceItemType itemType;
    private String referenceId;
    private String itemName;
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal subtotal;
}
