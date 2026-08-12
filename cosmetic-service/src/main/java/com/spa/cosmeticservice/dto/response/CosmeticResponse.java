package com.spa.cosmeticservice.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CosmeticResponse {
    private String id;
    private String name;
    private String brand;
    private String manufacturer;
    private BigDecimal price;
    private String description;
}
