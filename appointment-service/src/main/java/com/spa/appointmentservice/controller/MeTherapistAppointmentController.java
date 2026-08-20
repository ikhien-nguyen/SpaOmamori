package com.spa.appointmentservice.controller;

import com.spa.appointmentservice.client.ProfileClient;
import com.spa.appointmentservice.dto.request.CreateTherapyRecordRequest;
import com.spa.appointmentservice.dto.request.UpdateAppointmentStatusRequest;
import com.spa.appointmentservice.dto.response.ApiResponse;
import com.spa.appointmentservice.dto.response.AppointmentResponse;
import com.spa.appointmentservice.dto.response.TherapistInternalResponse;
import com.spa.appointmentservice.dto.response.TherapyRecordResponse;
import com.spa.appointmentservice.exception.AppException;
import com.spa.appointmentservice.exception.ErrorCode;
import com.spa.appointmentservice.service.AppointmentService;
import com.spa.appointmentservice.service.TherapyRecordService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Therapist self-service endpoints. therapistId is derived EXCLUSIVELY from the
 * JWT subject via Profile Service — never from the request body / query / path.
 * Ownership is re-checked on every status mutation so a KTV cannot act on
 * someone else's appointment.
 *
 * External Gateway paths:
 *   GET    /api/omamori/appointments/me
 *   GET    /api/omamori/appointments/me/{id}
 *   PATCH  /api/omamori/appointments/me/{id}/status
 */
@RestController
@RequestMapping("/therapist/me")
@RequiredArgsConstructor
@FieldDefaults(
        level = AccessLevel.PRIVATE,
        makeFinal = true
)
@PreAuthorize("hasAuthority('THERAPIST')")
public class MeTherapistAppointmentController {

    AppointmentService appointmentService;
    ProfileClient profileClient;
    TherapyRecordService therapyRecordService;

    @GetMapping
    public ApiResponse<List<AppointmentResponse>> myAppointments(
            @AuthenticationPrincipal Jwt jwt) {

        String therapistId = resolveTherapistId(jwt);

        return ApiResponse.<List<AppointmentResponse>>builder()
                .result(
                        appointmentService
                                .getAppointmentsByTherapist(
                                        therapistId
                                )
                )
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<AppointmentResponse> myAppointmentById(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String id) {

        String therapistId = resolveTherapistId(jwt);

        AppointmentResponse appointment =
                appointmentService.getAppointmentById(id);

        requireOwnership(appointment, therapistId);

        return ApiResponse.<AppointmentResponse>builder()
                .result(appointment)
                .build();
    }

    /**
     * KTV chỉ được đổi trạng thái lịch thuộc chính mình.
     *
     * therapistId từ client bị loại bỏ để KTV
     * không thể tự đổi người phụ trách.
     */
    @PatchMapping("/{id}/status")
    public ApiResponse<AppointmentResponse> updateMyAppointmentStatus(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String id,
            @Valid @RequestBody UpdateAppointmentStatusRequest request) {

        String therapistId = resolveTherapistId(jwt);

        AppointmentResponse appointment =
                appointmentService.getAppointmentById(id);

        requireOwnership(appointment, therapistId);

        String currentStatus =
                String.valueOf(appointment.getStatus());

        String nextStatus =
                String.valueOf(request.getStatus());

        boolean allowed =
                ("PENDING".equals(currentStatus)
                        && "CONFIRMED".equals(nextStatus))
                ||
                ("CONFIRMED".equals(currentStatus)
                        && "IN_PROGRESS".equals(nextStatus));

        if (!allowed) {
            throw new AppException(
                    ErrorCode.INVALID_APPOINTMENT_STATUS_TRANSITION
            );
        }

        // Không cho KTV thay đổi therapistId.
        request.setTherapistId(null);

        AppointmentResponse updated =
                appointmentService.updateStatus(id, request);

        return ApiResponse.<AppointmentResponse>builder()
                .result(updated)
                .build();
    }

    @PostMapping("/{id}/therapy-record")
    public ApiResponse<TherapyRecordResponse> createMyTherapyRecord(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String id,
            @Valid @RequestBody CreateTherapyRecordRequest request) {


        String therapistId = resolveTherapistId(jwt);


        AppointmentResponse appointment =
                appointmentService.getAppointmentById(id);


        requireOwnership(appointment, therapistId);


        if (!"IN_PROGRESS".equals(
                String.valueOf(appointment.getStatus())
        )) {
            throw new AppException(
                    ErrorCode.APPOINTMENT_NOT_IN_PROGRESS
            );
        }


        return ApiResponse.<TherapyRecordResponse>builder()
                .result(
                        therapyRecordService.createRecord(
                                id,
                                request
                        )
                )
                .build();
    }


    @GetMapping("/{id}/therapy-record")
    public ApiResponse<TherapyRecordResponse> getMyTherapyRecord(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String id) {


        String therapistId = resolveTherapistId(jwt);


        AppointmentResponse appointment =
                appointmentService.getAppointmentById(id);


        requireOwnership(appointment, therapistId);


        return ApiResponse.<TherapyRecordResponse>builder()
                .result(
                        therapyRecordService
                                .getRecordByAppointmentId(id)
                )
                .build();
    }

    private String resolveTherapistId(Jwt jwt) {
        String userId = jwt.getSubject();

        TherapistInternalResponse therapist =
                profileClient.getTherapistByUserId(userId);

        return therapist.getId();
    }

    private void requireOwnership(
            AppointmentResponse appointment,
            String therapistId) {

        if (appointment.getTherapistId() == null
                || !appointment.getTherapistId().equals(therapistId)) {
            throw new AppException(
                    ErrorCode.APPOINTMENT_ACCESS_DENIED
            );
        }
    }
}
