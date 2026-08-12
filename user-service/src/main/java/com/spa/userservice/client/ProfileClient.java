package com.spa.userservice.client;

import com.spa.userservice.dto.request.CreateProfileRequest;
import com.spa.userservice.dto.response.ApiResponse;
import com.spa.userservice.dto.response.ProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Client gọi sang Profile Service. profile-service có server.servlet.context-path=/profiles,
 * nên path đầy đủ của endpoint nội bộ là /profiles/internal (KHÔNG phải /internal/profiles).
 *
 * name = "profiles" phải khớp spring.application.name bên profile-service — Feign sẽ
 * tự tra địa chỉ instance qua Eureka (load-balanced) thay vì URL cứng như trước.
 */
@FeignClient(name = "profiles")
public interface ProfileClient {

    @PostMapping("/profiles/internal")
    ApiResponse<ProfileResponse> createProfile(@RequestBody CreateProfileRequest request);
}