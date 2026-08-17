package com.spa.notificationservice.messaging;

import com.spa.notificationservice.dto.event.InvoicePaidEvent;
import com.spa.notificationservice.entity.NotificationType;
import com.spa.notificationservice.service.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Lang nghe topic "invoice-paid" tu Payment Service (cung topic voi
 * cosmetic-service.InvoicePaidConsumer, khac group-id nen ca 2 service deu
 * nhan duoc du lieu) de tao thong bao IN_APP "Thanh toan thanh cong".
 *
 * LUU Y: field customerId/totalAmount PHAI duoc Payment Service publish kem
 * theo (xem payment-service/.../InvoicePaidPublisher) - neu Payment Service
 * chua sua de them 2 field nay, customerId se la null va consumer se bo qua
 * (log warn) thay vi tao thong bao rong.
 */
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class InvoicePaidConsumer {

    NotificationService notificationService;

    @KafkaListener(topics = "${notification-kafka.invoice-paid-topic}")
    public void onInvoicePaid(InvoicePaidEvent event) {
        log.info("Nhận sự kiện invoice-paid cho invoiceId={}", event.getInvoiceId());

        if (event.getCustomerId() == null || event.getCustomerId().isBlank()) {
            log.warn("invoice-paid event thiếu customerId (invoiceId={}) - bỏ qua, "
                    + "kiểm tra lại InvoicePaidPublisher bên payment-service", event.getInvoiceId());
            return;
        }

        BigDecimal amount = event.getTotalAmount() == null ? BigDecimal.ZERO : event.getTotalAmount();
        String content = String.format(
                "Hóa đơn %s đã được thanh toán thành công, số tiền %s VND. Cảm ơn quý khách!",
                event.getInvoiceId(), amount.toPlainString());

        notificationService.createInApp(
                event.getCustomerId(), NotificationType.INVOICE_PAID, "Thanh toán thành công", content);
    }
}
