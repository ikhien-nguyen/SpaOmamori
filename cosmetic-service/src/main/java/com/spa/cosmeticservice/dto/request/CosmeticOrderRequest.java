package com.spa.cosmeticservice.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CosmeticOrderRequest {

    // Không @NotBlank: cho phép bỏ trống khi kê đơn bán lẻ không gắn lịch hẹn
    // (đúng dấu N - nullable trên MaLichHen trong ERD DonMyPham).
    private String appointmentId;

    @NotBlank(message = "Vui lòng chọn kỹ thuật viên")
    private String technicianId;

    private String note;

    @NotEmpty(message = "Đơn kê phải có ít nhất 1 loại mỹ phẩm")
    @Valid
    private List<Item> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {
        @NotBlank(message = "Vui lòng chọn mỹ phẩm")
        private String cosmeticId;

        @NotNull(message = "Số lượng không được để trống")
        @Min(value = 1, message = "Số lượng phải lớn hơn 0")
        private Integer quantity;

        private String usageInstruction;
    }
}
