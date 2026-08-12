package com.spa.appointmentservice.dto.response;

import lombok.*;

import java.math.BigDecimal;

/**
 * Khớp shape với RoomResponse bên room-service trả về —
 * chỉ giữ field appointment-service thực sự cần dùng.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomResponse {
    private String id;
    private String name;
    private BigDecimal price;
    private Boolean isActive;
}
