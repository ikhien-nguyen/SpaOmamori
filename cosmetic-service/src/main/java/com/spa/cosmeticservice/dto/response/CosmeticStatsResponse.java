package com.spa.cosmeticservice.dto.response;

import lombok.*;

/**
 * Phục vụ Dashboard/Báo cáo của Admin (mục "Số mỹ phẩm có trong spa" và
 * "Tổng số lượng tồn kho" trong yêu cầu Báo cáo & Thống kê).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CosmeticStatsResponse {
    private long totalCosmeticTypes;
    private long totalInventoryQuantity;
}
