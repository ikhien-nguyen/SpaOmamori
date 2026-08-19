package com.spa.appointmentservice.controller;

import com.spa.appointmentservice.dto.response.ApiResponse;
import com.spa.appointmentservice.dto.response.AppointmentResponse;
import com.spa.appointmentservice.service.AppointmentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Service-to-service endpoints. Called directly by Feign clients
 * (e.g. payment-service -> GET /internal/{id} via AppointmentClient). These
 * endpoints are NOT intended for external callers — they are reachable only
 * via direct network from inside the cluster, not through the API Gateway
 * (the Gateway has a path-block filter that 404s any /internal/** under a
 * routed prefix).
 *
 * Callers are trusted; no ownership / role checks are applied here. The
 * public, ownership-safe counterpart for the customer self-service flow
 * remains MeAppointmentController.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/internal")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class InternalAppointmentController {

    AppointmentService appointmentService;

    @GetMapping("/{id}")
    public ApiResponse<AppointmentResponse> getAppointmentInternal(@PathVariable String id) {
        return ApiResponse.<AppointmentResponse>builder()
                .result(appointmentService.getAppointmentById(id))
                .build();
    }
}
