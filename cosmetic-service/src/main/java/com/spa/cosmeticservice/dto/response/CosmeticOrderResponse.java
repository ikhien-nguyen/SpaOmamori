package com.spa.cosmeticservice.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CosmeticOrderResponse {
    private String id;
    private String appointmentId;
    private String technicianId;
    private String note;
    private LocalDateTime createdAt;
    private List<Item> items;

    // Tổng tiền mỹ phẩm của cả đơn, tính theo giá SỐNG hiện tại của từng mỹ
    // phẩm (xem ghi chú ở CosmeticOrderItem) — Admin dùng số này làm tham
    // khảo khi lập hóa đơn, giá thật sự chốt ở HoaDonChiTiet.DonGia.
    private BigDecimal totalAmount;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Item {
        private String id;
        private String cosmeticId;
        private String cosmeticName;
        private int quantity;
        private String usageInstruction;
        private BigDecimal unitPrice;
        private BigDecimal lineTotal;
    }
}
