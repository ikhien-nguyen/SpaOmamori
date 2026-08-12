package com.spa.cosmeticservice.messaging;

import com.spa.cosmeticservice.dto.event.InvoicePaidEvent;
import com.spa.cosmeticservice.service.CosmeticInventoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Lắng nghe sự kiện "invoice-paid" từ Payment Service để trừ tồn kho — đây là
 * giao tiếp BẤT ĐỒNG BỘ duy nhất của Cosmetic Service (mục 11 style guide),
 * vì trừ kho không cần Payment Service phải đợi phản hồi ngay lập tức, và
 * Cosmetic Service có thể tạm ngưng/khởi động lại mà không làm rớt giao dịch
 * thanh toán (Kafka giữ lại message cho tới khi được xử lý).
 */
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class InvoicePaidConsumer {

    CosmeticInventoryService cosmeticInventoryService;

    @KafkaListener(topics = "${cosmetic.kafka.invoice-paid-topic}")
    public void onInvoicePaid(InvoicePaidEvent event) {
        log.info("Nhận sự kiện invoice-paid cho invoiceId={}", event.getInvoiceId());

        if (event.getItems() == null) {
            return;
        }

        for (InvoicePaidEvent.Item item : event.getItems()) {
            cosmeticInventoryService.deductStock(item.getCosmeticId(), item.getQuantity());
        }
    }
}
