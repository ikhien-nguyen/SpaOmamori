package com.spa.notificationservice.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoicePaidEvent {

    /**
     * Mã hóa đơn đã thanh toán
     */
    private String invoiceId;

    /**
     * Mã khách hàng sở hữu hóa đơn
     */
    private String customerId;

    /**
     * Tổng giá trị hóa đơn
     */
    private BigDecimal totalAmount;

    /**
     * Danh sách mỹ phẩm trong hóa đơn.
     *
     * Có thể rỗng nếu hóa đơn chỉ bao gồm
     * dịch vụ/phòng.
     */
    @Builder.Default
    private List<Item> items = List.of();

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {

        private String cosmeticId;

        private String name;

        private Integer quantity;

        private BigDecimal unitPrice;
    }
}