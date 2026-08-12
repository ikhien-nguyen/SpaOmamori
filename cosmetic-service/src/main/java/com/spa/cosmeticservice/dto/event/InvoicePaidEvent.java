package com.spa.cosmeticservice.dto.event;

import lombok.*;

import java.util.List;

/**
 * Payload sự kiện Kafka mà Payment Service publish lên topic "invoice-paid"
 * ngay sau khi xác nhận thanh toán thành công (áp dụng cho cả hóa đơn dịch vụ
 * lẫn hóa đơn bán lẻ mỹ phẩm — Cosmetic Service không cần phân biệt loại hóa
 * đơn, chỉ cần trừ đúng số lượng đã bán trong danh sách items).
 *
 * Đây là "hợp đồng" (contract) giữa 2 service, đội làm Payment Service cần
 * publish đúng cấu trúc JSON này lên topic cấu hình ở cosmetic.kafka.invoice-paid-topic.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoicePaidEvent {
    private String invoiceId;
    private List<Item> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Item {
        private String cosmeticId;
        private int quantity;
    }
}
