package com.spa.appointmentservice.entity;


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
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    // Toàn bộ 4 field dưới đây đều là liên kết LOGIC (không phải FK thật) vì
    // customerId/therapistId thuộc User Service, serviceId thuộc Treatment Service,
    // roomId thuộc Room Service — mỗi service có database riêng.
    @Column(name = "customer_id", nullable = false)
    String customerId;

    @Column(name = "therapist_id")
    String therapistId;

    @Column(name = "service_id", nullable = false)
    String serviceId;

    @Column(name = "room_id", nullable = false)
    String roomId;

    // Snapshot tên/giá tại thời điểm đặt lịch — tránh vỡ dữ liệu nếu sau này
    // Treatment/Room đổi giá, và để Payment Service tính hóa đơn không cần gọi lại.
    @Column(name = "service_name")
    String serviceName;

    @Column(name = "service_price", precision = 12, scale = 2)
    BigDecimal servicePrice;

    @Column(name = "room_name")
    String roomName;

    @Column(name = "room_price", precision = 12, scale = 2)
    BigDecimal roomPrice;

    @Column(name = "total_amount", precision = 12, scale = 2)
    BigDecimal totalAmount;

    @Column(name = "appointment_time", nullable = false)
    LocalDateTime appointmentTime;

    // Snapshot thời lượng dịch vụ (phút) tại thời điểm đặt lịch — dùng để tính
    // khung giờ [appointmentTime, appointmentTime + durationMinutes) khi kiểm
    // tra trùng lịch, tránh phải gọi lại treatment-service mỗi lần check.
    @Column(name = "duration_minutes", nullable = false)
    Integer durationMinutes;

    @Column(name = "reason")
    String reason;

    @Column(name = "note", columnDefinition = "TEXT")
    String note;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    AppointmentStatus status = AppointmentStatus.PENDING;

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

    // Not persisted — chỉ để service tính toán chồng lấn khung giờ.
    @Transient
    public LocalDateTime getEndTime() {
        return appointmentTime.plusMinutes(durationMinutes);
    }
}