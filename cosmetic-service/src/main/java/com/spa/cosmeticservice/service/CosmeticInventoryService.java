package com.spa.cosmeticservice.service;

import com.spa.cosmeticservice.dto.request.InventoryStockInRequest;
import com.spa.cosmeticservice.dto.request.InventoryUpdateRequest;
import com.spa.cosmeticservice.dto.response.CosmeticInventoryResponse;
import com.spa.cosmeticservice.dto.response.CosmeticStatsResponse;
import com.spa.cosmeticservice.entity.Cosmetic;
import com.spa.cosmeticservice.entity.CosmeticInventory;
import com.spa.cosmeticservice.entity.InventoryStatus;
import com.spa.cosmeticservice.exception.AppException;
import com.spa.cosmeticservice.exception.ErrorCode;
import com.spa.cosmeticservice.mapper.CosmeticInventoryMapper;
import com.spa.cosmeticservice.repository.CosmeticInventoryRepository;
import com.spa.cosmeticservice.repository.CosmeticRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CosmeticInventoryService {

    CosmeticInventoryRepository inventoryRepository;
    CosmeticRepository cosmeticRepository;
    CosmeticInventoryMapper inventoryMapper;
    CosmeticService cosmeticService;

    public List<CosmeticInventoryResponse> getAll() {
        return inventoryRepository.findAll().stream()
                .map(inventoryMapper::toResponse)
                .toList();
    }

    public CosmeticInventoryResponse stockIn(InventoryStockInRequest request) {
        Cosmetic cosmetic = cosmeticService.getCosmeticOrThrow(request.getCosmeticId());

        CosmeticInventory inventory = CosmeticInventory.builder()
                .cosmetic(cosmetic)
                .batchCode(request.getBatchCode())
                .quantity(request.getQuantity())
                .expiryDate(request.getExpiryDate())
                .status(InventoryStatus.AVAILABLE)
                .build();

        inventoryRepository.save(inventory);
        return inventoryMapper.toResponse(inventory);
    }

    public CosmeticInventoryResponse update(String id, InventoryUpdateRequest request) {
        CosmeticInventory inventory = getInventoryOrThrow(id);

        inventory.setQuantity(request.getQuantity());
        inventory.setExpiryDate(request.getExpiryDate());
        inventory.setStatus(resolveStatus(inventory));

        inventoryRepository.save(inventory);
        return inventoryMapper.toResponse(inventory);
    }

    public CosmeticStatsResponse getStats() {
        return CosmeticStatsResponse.builder()
                .totalCosmeticTypes(cosmeticRepository.count())
                .totalInventoryQuantity(inventoryRepository.sumAllQuantity())
                .build();
    }

    // Kiểm tra tồn kho khả dụng có đủ để kê đơn/bán không, dùng bởi
    // CosmeticPrescriptionService trước khi lưu đơn kê.
    boolean hasEnoughStock(String cosmeticId, int requiredQuantity) {
        return inventoryRepository.sumAvailableQuantity(cosmeticId) >= requiredQuantity;
    }

    /**
     * Trừ kho theo nguyên tắc FEFO (First-Expired-First-Out): trừ từ lô sắp hết hạn
     * nhất trước. Được gọi bởi InvoicePaidConsumer khi nhận sự kiện thanh toán thành
     * công từ Payment Service — KHÔNG gọi lúc kê đơn (kê đơn chỉ kiểm tra, không trừ).
     */
    @Transactional
    public void deductStock(String cosmeticId, int quantityToDeduct) {
        List<CosmeticInventory> batches = inventoryRepository
                .findByCosmetic_IdAndStatusOrderByExpiryDateAsc(cosmeticId, InventoryStatus.AVAILABLE);

        int remaining = quantityToDeduct;
        for (CosmeticInventory batch : batches) {
            if (remaining <= 0) break;

            int deductFromBatch = Math.min(remaining, batch.getQuantity());
            batch.setQuantity(batch.getQuantity() - deductFromBatch);
            remaining -= deductFromBatch;

            if (batch.getQuantity() == 0) {
                batch.setStatus(InventoryStatus.OUT_OF_STOCK);
            }
            inventoryRepository.save(batch);
        }

        if (remaining > 0) {
            // Về lý thuyết không nên xảy ra (đã kiểm tra hasEnoughStock lúc kê đơn),
            // nhưng vẫn log lại vì có thể có bán lẻ chen ngang làm tồn kho thay đổi
            // giữa lúc kê đơn và lúc thanh toán thực tế.
            log.warn("Trừ kho không đủ cho cosmeticId={}, còn thiếu {} đơn vị", cosmeticId, remaining);
        }
    }

    private InventoryStatus resolveStatus(CosmeticInventory inventory) {
        if (inventory.getQuantity() <= 0) {
            return InventoryStatus.OUT_OF_STOCK;
        }
        if (inventory.getExpiryDate() != null && !inventory.getExpiryDate().isAfter(LocalDate.now())) {
            return InventoryStatus.EXPIRED;
        }
        return InventoryStatus.AVAILABLE;
    }

    private CosmeticInventory getInventoryOrThrow(String id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.INVENTORY_NOT_FOUND));
    }
}
