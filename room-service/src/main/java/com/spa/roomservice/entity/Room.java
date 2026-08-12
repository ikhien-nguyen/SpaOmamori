package com.spa.roomservice.entity;


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
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "name", nullable = false)
    String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    RoomType type;

    @Column(name = "price", nullable = false, precision = 12, scale = 2)
    BigDecimal price;

    @Column(name = "capacity", nullable = false)
    Integer capacity;

    @Column(name = "note")
    String note;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    Boolean isActive = true;

    // Trang/bận hiện tại — appointment-service dong bo khi doi trang thai lich
    // hen (IN_PROGRESS -> OCCUPIED, COMPLETED/CANCELLED -> AVAILABLE lai).
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    RoomStatus status = RoomStatus.AVAILABLE;
}