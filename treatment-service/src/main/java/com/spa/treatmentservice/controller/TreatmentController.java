package com.spa.treatmentservice.controller;

import com.spa.treatmentservice.dto.request.TreatmentCreationRequest;
import com.spa.treatmentservice.dto.response.ApiResponse;
import com.spa.treatmentservice.dto.response.TreatmentResponse;
import com.spa.treatmentservice.service.TreatmentService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TreatmentController {

    TreatmentService treatmentService;

    @PostMapping
    public ApiResponse<TreatmentResponse> createTreatment(@Valid @RequestBody TreatmentCreationRequest request) {
        return ApiResponse.<TreatmentResponse>builder()
                .result(treatmentService.createTreatment(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<TreatmentResponse>> getAllTreatments() {
        return ApiResponse.<List<TreatmentResponse>>builder()
                .result(treatmentService.getAllActiveTreatments())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<TreatmentResponse> getTreatment(@PathVariable String id) {
        return ApiResponse.<TreatmentResponse>builder()
                .result(treatmentService.getTreatmentById(id))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deactivateTreatment(@PathVariable String id) {
        treatmentService.deactivateTreatment(id);
        return ApiResponse.<Void>builder().message("Đã ngừng bán dịch vụ").build();
    }

    // Endpoint nội bộ cho appointment-service gọi sang để validate + lấy giá khi đặt lịch
    @GetMapping("/internal/{id}")
    public ApiResponse<TreatmentResponse> getTreatmentInternal(@PathVariable String id) {
        return ApiResponse.<TreatmentResponse>builder()
                .result(treatmentService.getTreatmentById(id))
                .build();
    }
}