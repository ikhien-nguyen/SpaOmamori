package com.spa.appointmentservice.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

/**
 * Dữ liệu cho use case "Quản lý danh mục dịch vụ" (Admin tạo dịch vụ mới).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCreationRequest {

    @NotBlank(message = "Tên dịch vụ không được để trống")
    private String name;

    private String category;

    @NotNull(message = "Giá dịch vụ không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá dịch vụ phải lớn hơn 0")
    private BigDecimal price;

    @NotNull(message = "Thời lượng không được để trống")
    @Min(value = 1, message = "Thời lượng phải lớn hơn 0 phút")
    private Integer durationMinutes;

    private String description;
}