package com.spa.cosmeticservice.controller;

import com.spa.cosmeticservice.dto.request.CosmeticCreationRequest;
import com.spa.cosmeticservice.dto.request.CosmeticUpdateRequest;
import com.spa.cosmeticservice.dto.response.ApiResponse;
import com.spa.cosmeticservice.dto.response.CosmeticResponse;
import com.spa.cosmeticservice.service.CosmeticService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Ghi chú: khác với user-service, cosmetic-service (giống profile-service hiện tại)
 * CHƯA tích hợp Spring Security/JWT resource server để phân quyền Admin/Technician
 * ngay tại service này — cần bổ sung sau nếu nhóm muốn kiểm tra role ở từng service
 * thay vì chỉ ở API Gateway.
 */
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CosmeticController {

    CosmeticService cosmeticService;

    @GetMapping
    public ApiResponse<List<CosmeticResponse>> getAll() {
        return ApiResponse.<List<CosmeticResponse>>builder()
                .result(cosmeticService.getAll())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CosmeticResponse> getById(@PathVariable String id) {
        return ApiResponse.<CosmeticResponse>builder()
                .result(cosmeticService.getById(id))
                .build();
    }

    @PostMapping
    public ApiResponse<CosmeticResponse> create(@Valid @RequestBody CosmeticCreationRequest request) {
        return ApiResponse.<CosmeticResponse>builder()
                .message("Thêm mới thành công")
                .result(cosmeticService.create(request))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<CosmeticResponse> update(
            @PathVariable String id, @Valid @RequestBody CosmeticUpdateRequest request) {
        return ApiResponse.<CosmeticResponse>builder()
                .message("Cập nhật thành công")
                .result(cosmeticService.update(id, request))
                .build();
    }
}
