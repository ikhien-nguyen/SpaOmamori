package com.spa.cosmeticservice.service;

import com.spa.cosmeticservice.dto.request.CosmeticCreationRequest;
import com.spa.cosmeticservice.dto.request.CosmeticUpdateRequest;
import com.spa.cosmeticservice.dto.response.CosmeticResponse;
import com.spa.cosmeticservice.entity.Cosmetic;
import com.spa.cosmeticservice.exception.AppException;
import com.spa.cosmeticservice.exception.ErrorCode;
import com.spa.cosmeticservice.mapper.CosmeticMapper;
import com.spa.cosmeticservice.repository.CosmeticInventoryRepository;
import com.spa.cosmeticservice.repository.CosmeticRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CosmeticService {

    CosmeticRepository cosmeticRepository;
    CosmeticInventoryRepository cosmeticInventoryRepository;
    CosmeticMapper cosmeticMapper;

    // Không yêu cầu đăng nhập: use case "Xem danh sách mỹ phẩm" cho phép cả
    // Khách ghé thăm (chưa đăng nhập) xem được.
    public List<CosmeticResponse> getAll() {
        return cosmeticRepository.findAll().stream()
                .map(this::toResponseWithStock)
                .toList();
    }

    public CosmeticResponse getById(String id) {
        Cosmetic cosmetic = getCosmeticOrThrow(id);
        return toResponseWithStock(cosmetic);
    }

    public CosmeticResponse create(CosmeticCreationRequest request) {
        Cosmetic cosmetic = cosmeticMapper.toCosmetic(request);
        cosmeticRepository.save(cosmetic);
        return toResponseWithStock(cosmetic);
    }

    public CosmeticResponse update(String id, CosmeticUpdateRequest request) {
        Cosmetic cosmetic = getCosmeticOrThrow(id);
        cosmeticMapper.updateCosmetic(cosmetic, request);
        cosmeticRepository.save(cosmetic);
        return toResponseWithStock(cosmetic);
    }

    // Ghep them SoLuongTonKho (tinh song tu CosmeticInventory) vao response -
    // dung ERD ma khong phai luu du thua/de lech du lieu tren entity Cosmetic.
    private CosmeticResponse toResponseWithStock(Cosmetic cosmetic) {
        CosmeticResponse response = cosmeticMapper.toCosmeticResponse(cosmetic);
        response.setStockQuantity(cosmeticInventoryRepository.sumAvailableQuantity(cosmetic.getId()));
        return response;
    }

    // Dùng nội bộ bởi CosmeticInventoryService/CosmeticPrescriptionService,
    // ném lỗi ngay nếu không tìm thấy thay vì trả về null cho caller tự check.
    Cosmetic getCosmeticOrThrow(String id) {
        return cosmeticRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COSMETIC_NOT_FOUND));
    }
}
