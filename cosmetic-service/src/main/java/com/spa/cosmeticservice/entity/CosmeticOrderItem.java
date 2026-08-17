package com.spa.cosmeticservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * ChiTietDonMyPham - mỗi dòng là 1 loại mỹ phẩm trong 1 đơn kê (CosmeticOrder).
 * KHÔNG lưu unitPrice ở đây (ERD gốc không có cột này trong ChiTietDonMyPham) —
 * giá được lấy sống (live) từ Cosmetic.price khi cần hiển thị/tính tổng, và
 * chỉ thực sự bị "chốt" khi Admin lập hóa đơn (HoaDonChiTiet.DonGia). Nghĩa là
 * nếu Admin đổi giá mỹ phẩm giữa lúc kê đơn và lúc lập hóa đơn, số tiền sẽ theo
 * giá tại thời điểm lập hóa đơn — đây là đánh đổi có chủ đích để khớp ERD, cần
 * lưu ý khi làm phần Invoice Service.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "cosmetic_order_item")
public class CosmeticOrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cosmetic_order_id", nullable = false)
    CosmeticOrder order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cosmetic_id", nullable = false)
    Cosmetic cosmetic;

    @Column(name = "quantity")
    int quantity;

    @Column(name = "usage_instruction", columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    String usageInstruction;
}
