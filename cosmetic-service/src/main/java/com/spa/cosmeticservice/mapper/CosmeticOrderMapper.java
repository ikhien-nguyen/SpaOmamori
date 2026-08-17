package com.spa.cosmeticservice.mapper;

import com.spa.cosmeticservice.dto.response.CosmeticOrderResponse;
import com.spa.cosmeticservice.entity.CosmeticOrder;
import com.spa.cosmeticservice.entity.CosmeticOrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CosmeticOrderMapper {

    // items/totalAmount không map trực tiếp (cần tính unitPrice sống + lineTotal),
    // set thủ công ở CosmeticOrderService.
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    CosmeticOrderResponse toResponse(CosmeticOrder order);

    @Mapping(target = "cosmeticId", source = "cosmetic.id")
    @Mapping(target = "cosmeticName", source = "cosmetic.name")
    @Mapping(target = "unitPrice", ignore = true)
    @Mapping(target = "lineTotal", ignore = true)
    CosmeticOrderResponse.Item toItemResponse(CosmeticOrderItem item);
}
