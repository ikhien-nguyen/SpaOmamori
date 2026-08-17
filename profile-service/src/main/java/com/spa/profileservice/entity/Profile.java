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

    // Dung ERD: ThongTinCaNhan.HoTen (bat buoc). Ban ghi goc van o
    // User.fullName (user-service) - truong nay la BAN SAO dong bo tai thoi
    // diem tao profile / cap nhat, phuc vu cac API doc theo dung cau truc
    // ERD (ThongTinCaNhan co san HoTen) ma khong phai goi cheo sang
    // user-service moi lan doc.
    @Column(name = "full_name", nullable = false)
    String fullName;

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