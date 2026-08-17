package com.spa.notificationservice.entity;

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
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    // Lien ket logic sang User Service - khong phai FK that (khac database).
    @Column(name = "recipient_id", nullable = false)
    String recipientId;

    // Snapshot email tai thoi diem tao - dung de gui EMAIL ma khong can goi
    // nguoc lai User Service moi lan gui. Co the null neu channel = IN_APP.
    @Column(name = "recipient_email")
    String recipientEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    NotificationType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    NotificationChannel channel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    NotificationStatus status = NotificationStatus.PENDING;

    @Column(nullable = false)
    String title;

    @Column(columnDefinition = "TEXT")
    String content;

    // Chi co y nghia voi channel = IN_APP (hien chuong thong bao tren FE).
    @Column(name = "is_read", nullable = false)
    @Builder.Default
    boolean read = false;

    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    @Column(name = "sent_at")
    LocalDateTime sentAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
