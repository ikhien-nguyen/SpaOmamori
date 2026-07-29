package com.spa.userservice.client;

import com.spa.userservice.dto.request.CreateProfileRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Client gọi sang Profile Service. profile-service có server.servlet.context-path=/profiles,
 * nên path đầy đủ của endpoint nội bộ là /profiles/internal (KHÔNG phải /internal/profiles).
 */
@FeignClient(name = "profile-service", url = "${profile-service.url}")
public interface ProfileClient {

    @PostMapping("/profiles/internal")
    void createProfile(@RequestBody CreateProfileRequest request);
}