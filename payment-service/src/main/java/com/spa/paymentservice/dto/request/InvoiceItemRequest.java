package com.spa.paymentservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Dùng cho các dòng mỹ phẩm bán lẻ/kê đơn khi lập hóa đơn. Chỉ cần
 * cosmeticId + quantity — InvoiceService luôn gọi CosmeticClient lấy
 * giá/tên thật từ cosmetic-service để tránh gian lận giá.
 *
 * cosmeticId nay la BAT BUOC (dung ERD: HoaDonChiTiet.MaMatHang khong duoc
 * null) - truoc day cho phep de trong (kem theo name/unitPrice nhap tay) de
 * ho tro "san pham ban thu chua len danh muc", da bo huong do de dam bao
 * du lieu hoa don luon truy vet duoc ve dung 1 ban ghi trong danh muc My
 * Pham.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemRequest {

    @NotBlank(message = "Sản phẩm mỹ phẩm không được để trống")
    private String cosmeticId;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    private Integer quantity;
}
