package com.spa.userservice.entity;


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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "email", unique = true, columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    String email;

    String password;

    @Column(name = "full_name", columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    String fullName;

    @Enumerated(EnumType.STRING)
    Role role;

    // Trạng thái tài khoản: true = đang hoạt động, false = đã bị Admin vô hiệu hóa.
    // Builder.Default để các bản ghi tạo qua @Builder mặc định active=true nếu không set.
    @Builder.Default
    @Column(name = "active", nullable = false)
    boolean active = true;

    // Bo sung theo ERD: TaiKhoan.NgayTao (bat buoc). Truoc day bang User
    // khong co cot nay.
    @Column(name = "created_at", updatable = false, nullable = false)
    LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
