package com.spa.paymentservice.client;

import com.spa.paymentservice.dto.response.ApiResponse;
import com.spa.paymentservice.dto.response.CosmeticResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cosmetics")
public interface CosmeticClient {

    @GetMapping("/cosmetics/{id}")
    ApiResponse<CosmeticResponse> getCosmetic(@PathVariable("id") String id);
}