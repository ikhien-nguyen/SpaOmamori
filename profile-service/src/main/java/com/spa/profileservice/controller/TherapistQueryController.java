package com.spa.profileservice.controller;

import com.spa.profileservice.dto.response.ApiResponse;
import com.spa.profileservice.dto.response.TherapistResponse;
import com.spa.profileservice.service.TherapistQueryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/therapists")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TherapistQueryController {

    TherapistQueryService therapistQueryService;

    @GetMapping
    public ApiResponse<List<TherapistResponse>> getActiveTherapists() {
        return ApiResponse.<List<TherapistResponse>>builder()
                .result(therapistQueryService.getActiveTherapists())
                .build();
    }
}
