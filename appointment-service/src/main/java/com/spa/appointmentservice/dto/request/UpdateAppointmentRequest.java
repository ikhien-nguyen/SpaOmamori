package com.spa.appointmentservice.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Dùng cho khách hàng chỉnh sửa lịch hẹn của chính mình khi còn ở trạng thái
 * PENDING. Cho phép đổi lại dịch vụ/phòng/KTV/
 * thời gian, giống hệt field của AppointmentCreationRequest vì bản chất là
 * đặt lại các lựa chọn ban đầu.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAppointmentRequest {

    @NotBlank(message = "Vui lòng chọn dịch vụ")
    private String serviceId;

    @NotBlank(message = "Vui lòng chọn phòng")
    private String roomId;

    private String therapistId;

    @NotNull(message = "Thời gian hẹn không được để trống")
    @Future(message = "Thời gian hẹn phải ở tương lai")
    private LocalDateTime appointmentTime;

    private String reason;

    private String note;
}