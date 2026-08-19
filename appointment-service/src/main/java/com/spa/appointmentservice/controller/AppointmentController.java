package com.spa.appointmentservice.controller;

import com.spa.appointmentservice.dto.request.UpdateAppointmentStatusRequest;
import com.spa.appointmentservice.dto.response.ApiResponse;
import com.spa.appointmentservice.dto.response.AppointmentResponse;
import com.spa.appointmentservice.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Admin / Therapist endpoints for the appointment domain.
 *
 * Customer-facing booking / list / reschedule / cancel have been moved to
 * MeAppointmentController which derives identity from the JWT subject.
 * customerId is NEVER read from the request (body/query/path) in this
 * controller — list/get endpoints are scoped by the caller roles.
 */
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AppointmentController {

    AppointmentService appointmentService;

    /**
     * Admin / Therapist view of any customer's appointments.
     * CUSTOMER must use GET /appointments/me instead.
     */
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'THERAPIST')")
    public ApiResponse<List<AppointmentResponse>> getByCustomer(@PathVariable String customerId) {
        return ApiResponse.<List<AppointmentResponse>>builder()
                .result(appointmentService.getAppointmentsByCustomer(customerId))
                .build();
    }

    @GetMapping("/therapist/{therapistId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'THERAPIST')")
    public ApiResponse<List<AppointmentResponse>> getByTherapist(@PathVariable String therapistId) {
        return ApiResponse.<List<AppointmentResponse>>builder()
                .result(appointmentService.getAppointmentsByTherapist(therapistId))
                .build();
    }

    /**
     * Admin / Therapist read by id. Customers use GET /appointments/me/{id}
     * which enforces ownership against the JWT subject.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'THERAPIST')")
    public ApiResponse<AppointmentResponse> getById(@PathVariable String id) {
        return ApiResponse.<AppointmentResponse>builder()
                .result(appointmentService.getAppointmentById(id))
                .build();
    }

    /**
     * Status transitions such as PENDING -> CONFIRMED, CONFIRMED -> IN_PROGRESS,
     * IN_PROGRESS -> COMPLETED. CUSTOMER cannot drive this; they only create
     * (PENDING) and cancel their own PENDING/CONFIRMED appointment.
     */
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'THERAPIST')")
    public ApiResponse<AppointmentResponse> updateStatus(
            @PathVariable String id, @Valid @RequestBody UpdateAppointmentStatusRequest request) {
        return ApiResponse.<AppointmentResponse>builder()
                .result(appointmentService.updateStatus(id, request))
                .build();
    }
}
