package com.spa.cosmeticservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DonMyPham (bảng cha) - đơn kê mỹ phẩm cho khách. Trước đây gộp chung với
 * chi tiết thành 1 bảng CosmeticPrescription (mỗi dòng = 1 loại mỹ phẩm),
 * SAI so với ERD gốc của nhóm (có DonMyPham + ChiTietDonMyPham riêng) —
 * tách lại thành 2 bảng để 1 đơn có thể kê NHIỀU loại mỹ phẩm cùng lúc.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "cosmetic_order")
public class CosmeticOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    // appointmentId thuộc Appointment Service (database khác) — chỉ lưu id dạng
    // String, KHÔNG @ManyToOne xuyên service. unique=true: đúng ERD (MaLichHen có
    // dấu U), mỗi lịch hẹn tối đa 1 đơn kê. Cho phép null (dấu N trong ERD) để hỗ
    // trợ trường hợp bán lẻ mỹ phẩm không gắn với lịch hẹn cụ thể.
    @Column(name = "appointment_id", unique = true)
    String appointmentId;

    @Column(name = "technician_id", nullable = false)
    String technicianId;

    @Column(name = "note", columnDefinition = "TEXT COLLATE utf8mb4_unicode_ci")
    String note;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<CosmeticOrderItem> items = new ArrayList<>();

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Helper để giữ 2 chiều quan hệ đồng bộ khi thêm dòng chi tiết.
    public void addItem(CosmeticOrderItem item) {
        items.add(item);
        item.setOrder(this);
    }
}
