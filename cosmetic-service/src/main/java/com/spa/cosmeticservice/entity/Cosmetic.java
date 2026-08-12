package com.spa.cosmeticservice.entity;

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
@Table(name = "cosmetic")
public class Cosmetic {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "name", columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    String name;

    @Column(name = "brand", columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    String brand;

    @Column(name = "manufacturer", columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    String manufacturer;

    @Column(name = "price", precision = 12, scale = 2)
    BigDecimal price;

    @Column(name = "description", columnDefinition = "TEXT COLLATE utf8mb4_unicode_ci")
    String description;
}
