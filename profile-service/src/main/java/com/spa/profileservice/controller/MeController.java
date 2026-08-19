package com.spa.profileservice.controller;

import com.spa.profileservice.dto.request.UpdateProfileRequest;
import com.spa.profileservice.dto.response.ApiResponse;
import com.spa.profileservice.dto.response.ProfileResponse;
import com.spa.profileservice.service.ProfileService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Self-service endpoints for the authenticated customer. userId is derived
 * exclusively from the JWT subject (sub) — never from request body/query/path.
 * Distinct from /internal/** which is service-to-service only.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/me")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MeController {

    ProfileService profileService;

    @GetMapping
    public ApiResponse<ProfileResponse> getMyProfile(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        return ApiResponse.<ProfileResponse>builder()
                .result(profileService.getProfileByUserId(userId))
                .build();
    }

    @PutMapping
    public ApiResponse<ProfileResponse> updateMyProfile(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody UpdateProfileRequest request) {
        String userId = jwt.getSubject();
        return ApiResponse.<ProfileResponse>builder()
                .result(profileService.updateProfile(userId, request))
                .build();
    }
}
