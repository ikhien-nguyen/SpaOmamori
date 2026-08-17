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
    private String unit;
    private BigDecimal price;
    private String description;

    // Dung ERD: MyPham.SoLuongTonKho. KHONG luu lai o cot rieng tren
    // Cosmetic entity (se de bi lech du lieu voi CosmeticInventory) - tinh
    // song (live) tu tong so luong con AVAILABLE trong cac lo hang, xem
    // CosmeticService.
    private long stockQuantity;
}
