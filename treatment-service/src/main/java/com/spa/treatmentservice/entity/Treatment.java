package com.spa.treatmentservice.entity;


import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

/**
 * Danh mục dịch vụ trị liệu (bảng SERVICE trong thiết kế CSDL).
 * Đặt tên "Treatment" (không phải "Service") vì đây là service riêng biệt
 * (treatment-service), không còn nguy cơ đụng tên với @Service của Spring nữa,
 * nhưng vẫn giữ tên rõ nghĩa nghiệp vụ hơn "Service" chung chung.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Treatment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "category")
    String category;

    @Column(name = "price", nullable = false, precision = 12, scale = 2)
    BigDecimal price;

    @Column(name = "duration_minutes", nullable = false)
    Integer durationMinutes;

    @Column(name = "description", columnDefinition = "TEXT")
    String description;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    Boolean isActive = true;
}