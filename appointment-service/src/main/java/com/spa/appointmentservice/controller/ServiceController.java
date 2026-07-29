package com.spa.appointmentservice.controller;

import com.spa.appointmentservice.dto.request.ServiceCreationRequest;
import com.spa.appointmentservice.dto.response.ApiResponse;
import com.spa.appointmentservice.dto.response.ServiceResponse;
import com.spa.appointmentservice.service.ServiceCatalogService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/services")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ServiceController {

    ServiceCatalogService serviceCatalogService;

    @PostMapping
    public ApiResponse<ServiceResponse> createService(@Valid @RequestBody ServiceCreationRequest request) {
        return ApiResponse.<ServiceResponse>builder()
                .result(serviceCatalogService.createService(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<ServiceResponse>> getAllServices() {
        return ApiResponse.<List<ServiceResponse>>builder()
                .result(serviceCatalogService.getAllActiveServices())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ServiceResponse> getService(@PathVariable String id) {
        return ApiResponse.<ServiceResponse>builder()
                .result(serviceCatalogService.getServiceById(id))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deactivateService(@PathVariable String id) {
        serviceCatalogService.deactivateService(id);
        return ApiResponse.<Void>builder().message("Đã ngừng bán dịch vụ").build();
    }
}