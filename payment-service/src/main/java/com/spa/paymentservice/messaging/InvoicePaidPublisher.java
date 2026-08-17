package com.spa.paymentservice.messaging;

import com.spa.paymentservice.dto.event.InvoicePaidEvent;
import com.spa.paymentservice.entity.Invoice;
import com.spa.paymentservice.entity.InvoiceItemType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Publish sự kiện "invoice-paid" sang cosmetic-service để trừ tồn kho tự
 * động - đây là mảnh còn thiếu khiến deductStock() bên cosmetic-service
 * chưa từng được gọi dù consumer đã sẵn sàng từ trước.
 */
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class InvoicePaidPublisher {

    KafkaTemplate<String, InvoicePaidEvent> kafkaTemplate;

    @Value("${cosmetic.kafka.invoice-paid-topic}")
    String invoicePaidTopic;

    public void publish(Invoice invoice) {
        List<InvoicePaidEvent.Item> items = invoice.getItems().stream()
                .filter(item -> item.getItemType() == InvoiceItemType.COSMETIC)
                .filter(item -> item.getReferenceId() != null && !item.getReferenceId().isBlank())
                .map(item -> InvoicePaidEvent.Item.builder()
                        .cosmeticId(item.getReferenceId())
                        .quantity(item.getQuantity())
                        .build())
                .toList();

        if (items.isEmpty()) {
            return;
        }

        InvoicePaidEvent event = InvoicePaidEvent.builder()
                .invoiceId(invoice.getId())
                .items(items)
                .build();

        try {
            kafkaTemplate.send(invoicePaidTopic, invoice.getId(), event);
            log.info("Đã publish invoice-paid cho hóa đơn {} ({} dòng mỹ phẩm)", invoice.getId(), items.size());
        } catch (Exception e) {
            // Không để lỗi publish Kafka làm fail giao dịch thanh toán đã xác
            // nhận thành công - log để retry/đối soát thủ công sau.
            log.error("Publish invoice-paid thất bại cho hóa đơn {}: {}", invoice.getId(), e.getMessage());
        }
    }
}