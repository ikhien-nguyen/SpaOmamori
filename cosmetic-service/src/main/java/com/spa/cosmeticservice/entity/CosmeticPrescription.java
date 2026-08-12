package com.spa.cosmeticservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "cosmetic_prescription")
public class CosmeticPrescription {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    // appointmentId/technicianId thuộc Appointment Service/User Service khác — chỉ
    // lưu id dạng String ở đây, KHÔNG dùng @ManyToOne xuyên service.
    @Column(name = "appointment_id")
    String appointmentId;

    @Column(name = "technician_id")
    String technicianId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cosmetic_id", nullable = false)
    Cosmetic cosmetic;

    @Column(name = "quantity")
    int quantity;

    // Chốt giá tại thời điểm kê đơn, để sau này Payment/Invoice tính tiền không bị
    // ảnh hưởng nếu Admin đổi giá mỹ phẩm trong bảng COSMETIC sau đó.
    @Column(name = "unit_price", precision = 12, scale = 2)
    BigDecimal unitPrice;

    @Column(name = "prescribed_at")
    LocalDateTime prescribedAt;
}
