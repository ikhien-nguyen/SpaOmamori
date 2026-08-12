package com.spa.cosmeticservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CosmeticPrescriptionRequest {
    @NotBlank(message = "Vui lòng chọn lịch hẹn")
    private String appointmentId;

    @NotBlank(message = "Vui lòng chọn kỹ thuật viên")
    private String technicianId;

    @NotBlank(message = "Vui lòng chọn mỹ phẩm")
    private String cosmeticId;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    private Integer quantity;
}
