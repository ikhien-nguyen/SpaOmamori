package com.spa.appointmentservice.controller;

import com.spa.appointmentservice.dto.request.CreateTherapyProfileRequest;
import com.spa.appointmentservice.dto.request.UpdateTherapyProfileRequest;
import com.spa.appointmentservice.dto.response.ApiResponse;
import com.spa.appointmentservice.dto.response.TherapyProfileResponse;
import com.spa.appointmentservice.service.TherapyProfileService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * Du lieu suc khoe nhay cam - khi lam SecurityConfig cho service nay, PHAI
 * gioi han: chi Admin va Therapist duoc phan cong lich hen cua khach do moi
 * duoc GET, KHONG permitAll, KHONG de Customer/Therapist khac doc tuy y.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/therapy-profiles")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TherapyProfileController {

    TherapyProfileService therapyProfileService;

    @PostMapping("/{customerId}")
    public ApiResponse<TherapyProfileResponse> createProfile(
            @PathVariable String customerId, @Valid @RequestBody CreateTherapyProfileRequest request) {
        return ApiResponse.<TherapyProfileResponse>builder()
                .result(therapyProfileService.createProfile(customerId, request))
                .build();
    }

    @GetMapping("/{customerId}")
    public ApiResponse<TherapyProfileResponse> getProfile(@PathVariable String customerId) {
        return ApiResponse.<TherapyProfileResponse>builder()
                .result(therapyProfileService.getByCustomerId(customerId))
                .build();
    }

    @PutMapping("/{customerId}")
    public ApiResponse<TherapyProfileResponse> updateProfile(
            @PathVariable String customerId, @Valid @RequestBody UpdateTherapyProfileRequest request) {
        return ApiResponse.<TherapyProfileResponse>builder()
                .result(therapyProfileService.updateProfile(customerId, request))
                .build();
    }
}
