package com.spa.profileservice.controller;

import com.spa.profileservice.dto.request.CreateProfileRequest;
import com.spa.profileservice.dto.request.UpdateProfileRequest;
import com.spa.profileservice.dto.response.ApiResponse;
import com.spa.profileservice.dto.response.ProfileResponse;
import com.spa.profileservice.service.ProfileService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * Toàn bộ endpoint dưới "/internal" (full path: /profiles/internal/**, vì
 * context-path server đã là /profiles) chỉ dành cho giao tiếp service-to-service
 * (User Service gọi sang) — KHÔNG expose ra ngoài. Cần chặn prefix /internal/**
 * ngay ở API Gateway (không thêm route cho nó) để client bên ngoài không gọi trực tiếp được.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/internal")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProfileController {

    ProfileService profileService;

    @PostMapping
    public ApiResponse<ProfileResponse> createProfile(@Valid @RequestBody CreateProfileRequest request) {
        return ApiResponse.<ProfileResponse>builder()
                .result(profileService.createProfile(request))
                .build();
    }

    @GetMapping("/{userId}")
    public ApiResponse<ProfileResponse> getProfile(@PathVariable String userId) {
        return ApiResponse.<ProfileResponse>builder()
                .result(profileService.getProfileByUserId(userId))
                .build();
    }

    @PutMapping("/{userId}")
    public ApiResponse<ProfileResponse> updateProfile(
            @PathVariable String userId, @Valid @RequestBody UpdateProfileRequest request) {
        return ApiResponse.<ProfileResponse>builder()
                .result(profileService.updateProfile(userId, request))
                .build();
    }
}