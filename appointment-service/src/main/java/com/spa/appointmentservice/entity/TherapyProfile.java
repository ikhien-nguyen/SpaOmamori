package com.spa.appointmentservice.entity;


import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * Ho so da/di ung goc cua khach hang (bang HoSoTriLieu) - song lau dai,
 * TACH RIENG khoi TherapyRecord (NhatKyTriLieu - nhat ky tung buoi).
 * 1 khach hang chi co dung 1 ho so goc, dung xuyen suot nhieu lan tri lieu.
 *
 * LUU Y BAO MAT: day la du lieu suc khoe nhay cam (tinh trang da, tien su
 * benh/di ung) - chi Therapist duoc phan cong cho lich hen cua khach do +
 * Admin moi duoc doc, KHONG expose cong khai qua API public. Enforce quyen
 * nay o tang Controller/Security, khong chi dua vao role JWT don thuan.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class TherapyProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    // Lien ket logic sang User Service (khac database)
    @Column(name = "customer_id", nullable = false, unique = true)
    String customerId;

    @Column(name = "initial_skin_condition", columnDefinition = "TEXT")
    String initialSkinCondition;

    @Column(name = "medical_history", columnDefinition = "TEXT")
    String medicalHistory;

    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
