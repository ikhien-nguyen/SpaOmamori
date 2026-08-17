package com.spa.userservice.dto.response;

import com.spa.userservice.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response cho Admin xem danh sách/chi tiết người dùng (UC_09).
 * Không có trường password — UserCreationResponse trả cả password là rò rỉ
 * dữ liệu nhạy cảm không nên lặp lại ở đây.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private String id;
    private String fullName;
    private String email;
    private Role role;
    private boolean active;
    private LocalDateTime createdAt;
}
