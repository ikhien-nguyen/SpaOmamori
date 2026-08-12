package com.spa.appointmentservice.controller;

import com.spa.appointmentservice.dto.request.AppointmentCreationRequest;
import com.spa.appointmentservice.dto.request.UpdateAppointmentRequest;
import com.spa.appointmentservice.dto.request.UpdateAppointmentStatusRequest;
import com.spa.appointmentservice.dto.response.ApiResponse;
import com.spa.appointmentservice.dto.response.AppointmentResponse;
import com.spa.appointmentservice.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * customerId hiện đang nhận qua path/param tạm thời để test bằng Postman.
 * Khi API Gateway đã forward claim JWT xuống, đổi sang lấy từ
 * SecurityContext (Authentication#getName()) thay vì client tự truyền lên
 * — tránh khách hàng đặt lịch giùm tài khoản khác.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/appointments")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AppointmentController {

    AppointmentService appointmentService;

    @PostMapping
    public ApiResponse<AppointmentResponse> createAppointment(
            @RequestParam String customerId, @Valid @RequestBody AppointmentCreationRequest request) {
        return ApiResponse.<AppointmentResponse>builder()
                .result(appointmentService.createAppointment(customerId, request))
                .build();
    }

    @GetMapping("/customer/{customerId}")
    public ApiResponse<List<AppointmentResponse>> getByCustomer(@PathVariable String customerId) {
        return ApiResponse.<List<AppointmentResponse>>builder()
                .result(appointmentService.getAppointmentsByCustomer(customerId))
                .build();
    }

    @GetMapping("/therapist/{therapistId}")
    public ApiResponse<List<AppointmentResponse>> getByTherapist(@PathVariable String therapistId) {
        return ApiResponse.<List<AppointmentResponse>>builder()
                .result(appointmentService.getAppointmentsByTherapist(therapistId))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<AppointmentResponse> getById(@PathVariable String id) {
        return ApiResponse.<AppointmentResponse>builder()
                .result(appointmentService.getAppointmentById(id))
                .build();
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<AppointmentResponse> updateStatus(
            @PathVariable String id, @Valid @RequestBody UpdateAppointmentStatusRequest request) {
        return ApiResponse.<AppointmentResponse>builder()
                .result(appointmentService.updateStatus(id, request))
                .build();
    }

    // UC_05 - luong thay the 3a-3d: khach hang sua lich hen dang PENDING cua
    // chinh minh (doi gio/phong/dich vu/KTV).
    @PutMapping("/{id}")
    public ApiResponse<AppointmentResponse> updateAppointment(
            @PathVariable String id,
            @RequestParam String customerId,
            @Valid @RequestBody UpdateAppointmentRequest request) {
        return ApiResponse.<AppointmentResponse>builder()
                .result(appointmentService.updateAppointment(id, customerId, request))
                .build();
    }

    // UC_05 - luong thay the 3a1-3a4: khach hang huy lich hen cua chinh minh.
    @PatchMapping("/{id}/cancel")
    public ApiResponse<AppointmentResponse> cancelAppointment(
            @PathVariable String id, @RequestParam String customerId) {
        return ApiResponse.<AppointmentResponse>builder()
                .result(appointmentService.cancelAppointment(id, customerId))
                .build();
    }
}