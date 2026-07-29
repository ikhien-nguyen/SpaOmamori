package com.spa.appointmentservice.entity;


import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

/**
 * Danh mục dịch vụ/gói trị liệu (bảng SERVICES trong thiết kế CSDL).
 * Đặt tên "SpaService" thay vì "Service" để tránh trùng với annotation
 * @Service của Spring (dùng để đánh dấu class ở tầng service layer).
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "service")
public class SpaService {
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