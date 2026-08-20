package com.spa.appointmentservice.client;

import com.spa.appointmentservice.dto.response.TherapistInternalResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "profile-service",
        url = "${profile-service.url}"
)
public interface ProfileClient {

    @GetMapping(
            "/profiles/internal/therapists/by-user/{userId}"
    )
    TherapistInternalResponse getTherapistByUserId(
            @PathVariable("userId") String userId
    );
}
