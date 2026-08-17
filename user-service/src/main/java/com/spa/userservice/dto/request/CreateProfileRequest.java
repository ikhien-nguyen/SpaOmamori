package com.spa.userservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Payload User Service gửi sang Profile Service (nội bộ, không expose ra ngoài)
 * ngay sau khi tạo User thành công, để Profile Service tạo bản ghi PROFILE tương ứng.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProfileRequest {
    private String userId; // chính là User.id (UUID) vừa tạo bên User Service
    private String fullName; // dong bo tu User.fullName, dung ERD ThongTinCaNhan.HoTen
    private String role;
    private LocalDate dateOfBirth;
    private String gender;
    private String phone;
    private String address;
}