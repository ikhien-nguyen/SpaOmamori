package com.spa.cosmeticservice.controller;

import com.spa.cosmeticservice.dto.request.CosmeticOrderRequest;
import com.spa.cosmeticservice.dto.response.ApiResponse;
import com.spa.cosmeticservice.dto.response.CosmeticOrderResponse;
import com.spa.cosmeticservice.service.CosmeticOrderService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * DonMyPham (đơn kê mỹ phẩm) - đổi path từ /prescriptions cũ sang
 * /cosmetic-orders để khớp đúng khái niệm ERD (1 đơn có nhiều dòng
 * ChiTietDonMyPham, không còn 1 dòng = 1 loại mỹ phẩm như trước).
 * Nếu FE đã gọi /prescriptions, nhớ cập nhật lại theo path mới.
 */
@RestController
@RequestMapping("/cosmetic-orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CosmeticOrderController {

    CosmeticOrderService cosmeticOrderService;

    @PostMapping
    public ApiResponse<CosmeticOrderResponse> create(@Valid @RequestBody CosmeticOrderRequest request) {
        return ApiResponse.<CosmeticOrderResponse>builder()
                .message("Kê đơn thành công")
                .result(cosmeticOrderService.create(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CosmeticOrderResponse> getById(@PathVariable String id) {
        return ApiResponse.<CosmeticOrderResponse>builder()
                .result(cosmeticOrderService.getById(id))
                .build();
    }

    // Nội bộ nhưng KHÔNG đặt dưới /internal vì Admin (qua Gateway) cũng cần gọi
    // endpoint này để xem đơn kê đã ghi cho 1 lịch hẹn khi lập hóa đơn dịch vụ.
    @GetMapping("/appointment/{appointmentId}")
    public ApiResponse<CosmeticOrderResponse> getByAppointment(@PathVariable String appointmentId) {
        return ApiResponse.<CosmeticOrderResponse>builder()
                .result(cosmeticOrderService.getByAppointmentId(appointmentId))
                .build();
    }
}
