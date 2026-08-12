package com.spa.cosmeticservice.mapper;

import com.spa.cosmeticservice.dto.request.CosmeticCreationRequest;
import com.spa.cosmeticservice.dto.request.CosmeticUpdateRequest;
import com.spa.cosmeticservice.dto.response.CosmeticResponse;
import com.spa.cosmeticservice.entity.Cosmetic;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CosmeticMapper {
    Cosmetic toCosmetic(CosmeticCreationRequest request);

    CosmeticResponse toCosmeticResponse(Cosmetic cosmetic);

    void updateCosmetic(@MappingTarget Cosmetic cosmetic, CosmeticUpdateRequest request);
}
