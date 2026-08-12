package com.spa.appointmentservice.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

/**
 * customerId lấy từ JWT token ở tầng Controller, KHÔNG lấy từ client gửi lên.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentCreationRequest {

    @NotBlank(message = "Vui lòng chọn dịch vụ")
    private String serviceId;

    @NotBlank(message = "Vui lòng chọn phòng")
    private String roomId;

    private String therapistId; // có thể chưa chọn, Admin phân công sau

    @NotNull(message = "Thời gian hẹn không được để trống")
    @Future(message = "Thời gian hẹn phải ở tương lai")
    private LocalDateTime appointmentTime;

    private String reason;

    private String note;
}
