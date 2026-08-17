package com.spa.appointmentservice.entity;


import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class TherapyRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false, unique = true)
    Appointment appointment;

    // lien ket toi ho so goc cua khach hang (HoSoTriLieu)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "therapy_profile_id", nullable = false)
    TherapyProfile therapyProfile;

    // Đúng ERD (NhatKyTriLieu có MaKyThuatVien trực tiếp) — trước đây chỉ suy ra
    // gián tiếp qua appointment.therapistId. Chốt lại KTV thực hiện tại thời điểm
    // ghi nhật ký, để nếu sau này lịch hẹn bị đổi KTV thì nhật ký cũ vẫn đúng
    // người đã thực hiện, không bị thay đổi theo appointment.
    @Column(name = "technician_id", nullable = false)
    String technicianId;

    @Column(name = "condition_notes", columnDefinition = "TEXT")
    String conditionNotes;

    @Column(name = "improvement_notes", columnDefinition = "TEXT")
    String improvementNotes;

    @Column(name = "remaining_sessions")
    Integer remainingSessions;

    @Column(name = "recorded_at")
    LocalDateTime recordedAt;

    @PrePersist
    void onCreate() {
        recordedAt = LocalDateTime.now();
    }
}
