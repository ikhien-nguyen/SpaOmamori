package com.spa.appointmentservice.dto.request;

import com.spa.appointmentservice.entity.AppointmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAppointmentStatusRequest {

    @NotNull(message = "Trạng thái không được để trống")
    private AppointmentStatus status;

    private String therapistId; // dùng khi Admin phân công kỹ thuật viên cùng lúc đổi trạng thái
}
