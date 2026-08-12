package com.spa.appointmentservice.client;

import com.spa.appointmentservice.dto.response.ApiResponse;
import com.spa.appointmentservice.dto.response.TreatmentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "treatment-service", url = "${treatment-service.url}")
public interface TreatmentClient {

    @GetMapping("/treatments/internal/{id}")
    ApiResponse<TreatmentResponse> getTreatment(@PathVariable("id") String id);
}
