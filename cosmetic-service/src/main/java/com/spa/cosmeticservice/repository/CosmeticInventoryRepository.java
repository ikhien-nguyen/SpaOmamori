package com.spa.cosmeticservice.repository;

import com.spa.cosmeticservice.entity.CosmeticInventory;
import com.spa.cosmeticservice.entity.InventoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CosmeticInventoryRepository extends JpaRepository<CosmeticInventory, String> {

    // Sắp xếp theo hạn sử dụng tăng dần để trừ kho theo nguyên tắc FEFO
    // (First-Expired-First-Out) — lô sắp hết hạn được bán/dùng trước.
    List<CosmeticInventory> findByCosmetic_IdAndStatusOrderByExpiryDateAsc(
            String cosmeticId, InventoryStatus status);

    @Query("select coalesce(sum(i.quantity), 0) from CosmeticInventory i " +
            "where i.cosmetic.id = :cosmeticId and i.status = 'AVAILABLE'")
    long sumAvailableQuantity(@Param("cosmeticId") String cosmeticId);

    @Query("select coalesce(sum(i.quantity), 0) from CosmeticInventory i")
    long sumAllQuantity();
}
