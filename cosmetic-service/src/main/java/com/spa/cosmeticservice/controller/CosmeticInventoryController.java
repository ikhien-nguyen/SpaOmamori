package com.spa.cosmeticservice.controller;

import com.spa.cosmeticservice.dto.request.InventoryStockInRequest;
import com.spa.cosmeticservice.dto.request.InventoryUpdateRequest;
import com.spa.cosmeticservice.dto.response.ApiResponse;
import com.spa.cosmeticservice.dto.response.CosmeticInventoryResponse;
import com.spa.cosmeticservice.dto.response.CosmeticStatsResponse;
import com.spa.cosmeticservice.service.CosmeticInventoryService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CosmeticInventoryController {

    CosmeticInventoryService cosmeticInventoryService;

    @GetMapping
    public ApiResponse<List<CosmeticInventoryResponse>> getAll() {
        return ApiResponse.<List<CosmeticInventoryResponse>>builder()
                .result(cosmeticInventoryService.getAll())
                .build();
    }

    @PostMapping
    public ApiResponse<CosmeticInventoryResponse> stockIn(@Valid @RequestBody InventoryStockInRequest request) {
        return ApiResponse.<CosmeticInventoryResponse>builder()
                .message("Thêm hàng tồn thành công")
                .result(cosmeticInventoryService.stockIn(request))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<CosmeticInventoryResponse> update(
            @PathVariable String id, @Valid @RequestBody InventoryUpdateRequest request) {
        return ApiResponse.<CosmeticInventoryResponse>builder()
                .message("Cập nhật tồn kho thành công")
                .result(cosmeticInventoryService.update(id, request))
                .build();
    }

    @GetMapping("/stats")
    public ApiResponse<CosmeticStatsResponse> getStats() {
        return ApiResponse.<CosmeticStatsResponse>builder()
                .result(cosmeticInventoryService.getStats())
                .build();
    }
}
