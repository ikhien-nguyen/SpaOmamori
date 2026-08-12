package com.spa.cosmeticservice.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryStockInRequest {
    @NotBlank(message = "Vui lòng chọn mỹ phẩm")
    private String cosmeticId;

    @NotBlank(message = "Mã lô không được để trống")
    private String batchCode;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    private Integer quantity;

    @NotNull(message = "Ngày hết hạn không được để trống")
    @Future(message = "Ngày hết hạn phải sau ngày hiện tại")
    private LocalDate expiryDate;
}
