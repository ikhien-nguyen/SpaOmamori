package com.spa.paymentservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

/**
 * Dùng cho các dòng mỹ phẩm bán lẻ/kê đơn khi lập hóa đơn. Nếu có cosmeticId,
 * unitPrice/name gửi lên CHỈ mang tính tham khảo — InvoiceService sẽ gọi
 * CosmeticClient lấy giá/tên thật từ cosmetic-service để tránh gian lận giá.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemRequest {

    // Ma my pham ben Cosmetic Service - de trong neu chua co (VD: san pham
    // ban thu, chua len danh muc).
    private String cosmeticId;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String name;

    @NotNull(message = "Đơn giá không được để trống")
    @Positive(message = "Đơn giá phải lớn hơn 0")
    private BigDecimal unitPrice;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    private Integer quantity;
}
