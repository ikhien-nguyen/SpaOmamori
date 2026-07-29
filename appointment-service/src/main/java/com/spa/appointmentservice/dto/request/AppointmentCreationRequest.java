package com.spa.appointmentservice.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Dữ liệu cho use case "Đặt lịch hẹn" (Customer).
 * customerId lấy từ JWT token ở tầng Controller/Gateway, KHÔNG lấy từ client gửi lên
 * (tránh khách hàng tự đặt lịch giùm người khác).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentCreationRequest {

    @NotBlank(message = "Vui lòng chọn dịch vụ")
    private String serviceId;

    private String therapistId; // có thể chưa chọn, Admin phân công sau

    @NotNull(message = "Thời gian hẹn không được để trống")
    @Future(message = "Thời gian hẹn phải ở tương lai")
    private LocalDateTime appointmentTime;

    private String reason;

    private String note;
}