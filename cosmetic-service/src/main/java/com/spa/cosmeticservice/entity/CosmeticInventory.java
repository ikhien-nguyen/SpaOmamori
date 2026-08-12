package com.spa.cosmeticservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "cosmetic_inventory")
public class CosmeticInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    // Cùng 1 service (cosmetic-service) nên dùng quan hệ JPA thật, khác với
    // tham chiếu sang service khác (chỉ lưu id dạng String, VD: appointmentId).
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cosmetic_id", nullable = false)
    Cosmetic cosmetic;

    @Column(name = "batch_code", columnDefinition = "VARCHAR(100) COLLATE utf8mb4_unicode_ci")
    String batchCode;

    @Column(name = "quantity")
    int quantity;

    @Column(name = "expiry_date")
    LocalDate expiryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    InventoryStatus status;
}
