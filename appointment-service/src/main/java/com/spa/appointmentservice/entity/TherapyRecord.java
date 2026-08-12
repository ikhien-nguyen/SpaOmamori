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

    // MOI THEM: lien ket toi ho so goc cua khach hang (HoSoTriLieu)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "therapy_profile_id", nullable = false)
    TherapyProfile therapyProfile;

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
