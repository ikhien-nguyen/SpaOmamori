package com.spa.paymentservice.dto.event;

import lombok.*;

import java.util.List;

/**
 * "Hợp đồng" JSON với cosmetic-service (InvoicePaidConsumer) - PHẢI giữ đúng
 * tên field invoiceId/items/cosmeticId/quantity, vì consumer bên đó cấu hình
 * spring.json.use.type.headers: false nên deserialize theo cấu trúc JSON,
 * không quan tâm package/class name gửi lên.
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