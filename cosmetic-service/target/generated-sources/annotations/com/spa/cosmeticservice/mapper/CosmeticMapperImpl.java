package com.spa.cosmeticservice.mapper;

import com.spa.cosmeticservice.dto.request.CosmeticCreationRequest;
import com.spa.cosmeticservice.dto.request.CosmeticUpdateRequest;
import com.spa.cosmeticservice.dto.response.CosmeticResponse;
import com.spa.cosmeticservice.entity.Cosmetic;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-17T20:06:08+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23-valhalla (Oracle Corporation)"
)
@Component
public class CosmeticMapperImpl implements CosmeticMapper {

    @Override
    public Cosmetic toCosmetic(CosmeticCreationRequest request) {
        if ( request == null ) {
            return null;
        }

        Cosmetic.CosmeticBuilder cosmetic = Cosmetic.builder();

        cosmetic.name( request.getName() );
        cosmetic.brand( request.getBrand() );
        cosmetic.manufacturer( request.getManufacturer() );
        cosmetic.price( request.getPrice() );
        cosmetic.description( request.getDescription() );

        return cosmetic.build();
    }

    @Override
    public CosmeticResponse toCosmeticResponse(Cosmetic cosmetic) {
        if ( cosmetic == null ) {
            return null;
        }

        CosmeticResponse.CosmeticResponseBuilder cosmeticResponse = CosmeticResponse.builder();

        cosmeticResponse.id( cosmetic.getId() );
        cosmeticResponse.name( cosmetic.getName() );
        cosmeticResponse.brand( cosmetic.getBrand() );
        cosmeticResponse.manufacturer( cosmetic.getManufacturer() );
        cosmeticResponse.price( cosmetic.getPrice() );
        cosmeticResponse.description( cosmetic.getDescription() );

        return cosmeticResponse.build();
    }

    @Override
    public void updateCosmetic(Cosmetic cosmetic, CosmeticUpdateRequest request) {
        if ( request == null ) {
            return;
        }

        cosmetic.setName( request.getName() );
        cosmetic.setBrand( request.getBrand() );
        cosmetic.setManufacturer( request.getManufacturer() );
        cosmetic.setPrice( request.getPrice() );
        cosmetic.setDescription( request.getDescription() );
    }
}
