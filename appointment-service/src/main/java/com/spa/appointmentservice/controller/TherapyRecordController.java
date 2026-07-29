package com.spa.appointmentservice.controller;

import com.spa.appointmentservice.dto.request.CreateTherapyRecordRequest;
import com.spa.appointmentservice.dto.response.ApiResponse;
import com.spa.appointmentservice.dto.response.TherapyRecordResponse;
import com.spa.appointmentservice.service.TherapyRecordService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/appointments/{appointmentId}/therapy-record")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TherapyRecordController {

    TherapyRecordService therapyRecordService;

    @PostMapping
    public ApiResponse<TherapyRecordResponse> createRecord(
            @PathVariable String appointmentId, @Valid @RequestBody CreateTherapyRecordRequest request) {
        return ApiResponse.<TherapyRecordResponse>builder()
                .result(therapyRecordService.createRecord(appointmentId, request))
                .build();
    }

    @GetMapping
    public ApiResponse<TherapyRecordResponse> getRecord(@PathVariable String appointmentId) {
        return ApiResponse.<TherapyRecordResponse>builder()
                .result(therapyRecordService.getRecordByAppointmentId(appointmentId))
                .build();
    }
}