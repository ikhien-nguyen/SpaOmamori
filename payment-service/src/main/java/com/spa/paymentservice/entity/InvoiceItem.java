package com.spa.paymentservice.entity;


import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class InvoiceItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    Invoice invoice;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_type", nullable = false)
    InvoiceItemType itemType;

    // serviceId/roomId/cosmeticId ben service tuong ung - de tra cuu lai neu
    // can, khong phai FK that (khac database).
    // Dung ERD (MaMatHang khong co dau N => bat buoc). Truoc day cho phep
    // null de ho tro "san pham ban thu chua len danh muc" - da bo huong do,
    // xem InvoiceService.attachCosmeticItems + InvoiceItemRequest.
    @Column(name = "reference_id", nullable = false)
    String referenceId;

    // Snapshot ten tai thoi diem lap hoa don - khong doi du sau nay gia/ten
    // ben Treatment/Room/Cosmetic Service co thay doi.
    @Column(name = "item_name", nullable = false)
    String itemName;

    @Column(name = "unit_price", nullable = false, precision = 12, scale = 2)
    BigDecimal unitPrice;

    @Column(nullable = false)
    @Builder.Default
    Integer quantity = 1;

    @Column(nullable = false, precision = 12, scale = 2)
    BigDecimal subtotal;
}
