package com.spa.cosmeticservice.dto.response;

import com.spa.cosmeticservice.entity.InventoryStatus;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CosmeticInventoryResponse {
    private String id;
    private String cosmeticId;
    private String cosmeticName;
    private String manufacturer;
    private String batchCode;
    private int quantity;
    private LocalDate expiryDate;
    private InventoryStatus status;
}
