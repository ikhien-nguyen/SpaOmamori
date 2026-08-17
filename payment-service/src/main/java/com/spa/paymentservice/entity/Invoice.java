package com.spa.paymentservice.entity;


import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    // Lien ket logic sang User Service - moi hoa don PHAI gan dung khach
    // hang (dieu kien dac biet UC_11).
    @Column(name = "customer_id", nullable = false)
    String customerId;

    // Lien ket logic sang Appointment Service - null neu day la hoa don
    // ban le my pham, khong gan voi lich hen nao.
    // unique = true: dung ERD (MaLichHen co dau U) - 1 lich hen toi da 1 hoa
    // don. Truoc day chi check unique o tang Service
    // (existsByAppointmentId), khong co rang buoc that o DB nen van co the
    // bi duplicate khi 2 request tao hoa don cung luc cho 1 lich hen.
    @Column(name = "appointment_id", unique = true)
    String appointmentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    InvoiceStatus status = InvoiceStatus.PENDING_PAYMENT;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    PaymentMethod paymentMethod;

    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    BigDecimal totalAmount;

    // Ma tham chieu giao dich gui sang VNPay (vnp_TxnRef) - dung de doi
    // chieu khi VNPay goi IPN callback ve. Moi lan tao lai URL thanh toan se
    // sinh ma moi (khach bam "Thanh toan lai" thi ghi de len ma cu).
    @Column(name = "vnp_txn_ref")
    String vnpTxnRef;

    // Ma giao dich phia VNPay tra ve (vnp_TransactionNo) - luu lai de doi
    // soat/tra cuu sau nay khi co khieu nai.
    @Column(name = "vnp_transaction_no")
    String vnpTransactionNo;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<InvoiceItem> items = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    @Column(name = "paid_at")
    LocalDateTime paidAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Helper 2 chieu de gan invoice cho tung item khi build - tranh quen set
    // ben InvoiceItem roi bi loi FK null khi save.
    public void addItem(InvoiceItem item) {
        items.add(item);
        item.setInvoice(this);
    }
}
