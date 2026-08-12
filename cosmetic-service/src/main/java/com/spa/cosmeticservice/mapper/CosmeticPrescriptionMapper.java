package com.spa.cosmeticservice.mapper;

import com.spa.cosmeticservice.dto.response.CosmeticPrescriptionResponse;
import com.spa.cosmeticservice.entity.CosmeticPrescription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CosmeticPrescriptionMapper {

    @Mapping(target = "cosmeticId", source = "cosmetic.id")
    @Mapping(target = "cosmeticName", source = "cosmetic.name")
    // totalPrice không map trực tiếp từ field nào của entity (là unitPrice * quantity),
    // nên phải set thủ công ở Service sau khi map xong phần còn lại.
    @Mapping(target = "totalPrice", ignore = true)
    CosmeticPrescriptionResponse toResponse(CosmeticPrescription prescription);
}
