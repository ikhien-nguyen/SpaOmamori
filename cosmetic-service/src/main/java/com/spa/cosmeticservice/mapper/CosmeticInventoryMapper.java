package com.spa.cosmeticservice.mapper;

import com.spa.cosmeticservice.dto.response.CosmeticInventoryResponse;
import com.spa.cosmeticservice.entity.CosmeticInventory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CosmeticInventoryMapper {

    @Mapping(target = "cosmeticId", source = "cosmetic.id")
    @Mapping(target = "cosmeticName", source = "cosmetic.name")
    @Mapping(target = "manufacturer", source = "cosmetic.manufacturer")
    CosmeticInventoryResponse toResponse(CosmeticInventory inventory);
}
