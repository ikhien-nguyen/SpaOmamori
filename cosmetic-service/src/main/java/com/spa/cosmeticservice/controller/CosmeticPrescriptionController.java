package com.spa.cosmeticservice.controller;

import com.spa.cosmeticservice.dto.request.CosmeticPrescriptionRequest;
import com.spa.cosmeticservice.dto.response.ApiResponse;
import com.spa.cosmeticservice.dto.response.CosmeticPrescriptionResponse;
import com.spa.cosmeticservice.service.CosmeticPrescriptionService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prescriptions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CosmeticPrescriptionController {

    CosmeticPrescriptionService cosmeticPrescriptionService;

    @PostMapping
    public ApiResponse<CosmeticPrescriptionResponse> create(
            @Valid @RequestBody CosmeticPrescriptionRequest request) {
        return ApiResponse.<CosmeticPrescriptionResponse>builder()
                .message("Kê đơn thành công")
                .result(cosmeticPrescriptionService.create(request))
                .build();
    }

    // Nội bộ nhưng KHÔNG đặt dưới /internal vì Admin (qua Gateway) cũng cần gọi
    // endpoint này để xem đơn kê đã ghi cho 1 lịch hẹn khi lập hóa đơn dịch vụ.
    @GetMapping("/appointment/{appointmentId}")
    public ApiResponse<List<CosmeticPrescriptionResponse>> getByAppointment(
            @PathVariable String appointmentId) {
        return ApiResponse.<List<CosmeticPrescriptionResponse>>builder()
                .result(cosmeticPrescriptionService.getByAppointmentId(appointmentId))
                .build();
    }
}
