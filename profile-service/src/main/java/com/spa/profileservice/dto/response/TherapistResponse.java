package com.spa.profileservice.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TherapistResponse {

    // ID nghiệp vụ của bảng therapist.
    // Đây là ID sẽ được gửi sang Appointment.therapistId.
    private String id;

    // ID tài khoản bên User Service.
    private String userId;

    // Thông tin hiển thị lấy từ Profile.
    private String fullName;

    // Thông tin nghiệp vụ lấy từ Therapist.
    private String specialization;
    private String certificate;
    private String experience;

    // Ảnh hồ sơ lấy từ Profile.
    private String avatarUrl;

    private boolean active;
}
