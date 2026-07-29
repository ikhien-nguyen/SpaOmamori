package com.spa.profileservice.entity;


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
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    // Không có FK thật vì userId thuộc về User Service (database khác) —
    // đây là liên kết logic, luôn phải validate ở tầng Service khi cần.
    @Column(name = "user_id", unique = true, nullable = false)
    String userId;

    @Column(name = "date_of_birth")
    LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    Gender gender;

    @Column(name = "phone")
    String phone;

    @Column(name = "address", columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    String address;

    @Column(name = "avatar_url")
    String avatarUrl;
}