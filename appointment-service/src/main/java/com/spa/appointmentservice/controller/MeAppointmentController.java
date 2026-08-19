package com.spa.appointmentservice.controller;

import com.spa.appointmentservice.dto.request.AppointmentCreationRequest;
import com.spa.appointmentservice.dto.request.UpdateAppointmentRequest;
import com.spa.appointmentservice.dto.response.ApiResponse;
import com.spa.appointmentservice.dto.response.AppointmentResponse;
import com.spa.appointmentservice.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Customer self-service endpoints. customerId is derived EXCLUSIVELY from the
 * JWT subject (sub) — never from the request body / query / path. Ownership
 * for reschedule and cancel is re-checked across the service layer.
 *
 * External Gateway paths:
 *   POST   /api/omamori/appointments/me
 *   GET    /api/omamori/appointments/me
 *   GET    /api/omamori/appointments/me/{id}
 *   PUT    /api/omamori/appointments/me/{id}
 *   PATCH  /api/omamori/appointments/me/{id}/cancel
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/me")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@PreAuthorize("hasAuthority('CUSTOMER')")
public class MeAppointmentController {

    AppointmentService appointmentService;

    @PostMapping
    public ApiResponse<AppointmentResponse> createMyAppointment(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody AppointmentCreationRequest request) {
        String customerId = jwt.getSubject();
        return ApiResponse.<AppointmentResponse>builder()
                .result(appointmentService.createAppointment(customerId, request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<AppointmentResponse>> myAppointments(@AuthenticationPrincipal Jwt jwt) {
        String customerId = jwt.getSubject();
        return ApiResponse.<List<AppointmentResponse>>builder()
                .result(appointmentService.getAppointmentsByCustomer(customerId))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<AppointmentResponse> myAppointmentById(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String id) {
        String customerId = jwt.getSubject();
        return ApiResponse.<AppointmentResponse>builder()
                .result(appointmentService.getMyAppointmentById(customerId, id))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<AppointmentResponse> updateMyAppointment(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String id,
            @Valid @RequestBody UpdateAppointmentRequest request) {
        String customerId = jwt.getSubject();
        return ApiResponse.<AppointmentResponse>builder()
                .result(appointmentService.updateAppointment(id, customerId, request))
                .build();
    }

    @PatchMapping("/{id}/cancel")
    public ApiResponse<AppointmentResponse> cancelMyAppointment(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String id) {
        String customerId = jwt.getSubject();
        return ApiResponse.<AppointmentResponse>builder()
                .result(appointmentService.cancelAppointment(id, customerId))
                .build();
    }
}
