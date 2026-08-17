package com.spa.cosmeticservice.mapper;

import com.spa.cosmeticservice.dto.response.CosmeticInventoryResponse;
import com.spa.cosmeticservice.entity.Cosmetic;
import com.spa.cosmeticservice.entity.CosmeticInventory;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-17T20:06:08+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23-valhalla (Oracle Corporation)"
)
@Component
public class CosmeticInventoryMapperImpl implements CosmeticInventoryMapper {

    @Override
    public CosmeticInventoryResponse toResponse(CosmeticInventory inventory) {
        if ( inventory == null ) {
            return null;
        }

        CosmeticInventoryResponse.CosmeticInventoryResponseBuilder cosmeticInventoryResponse = CosmeticInventoryResponse.builder();

        cosmeticInventoryResponse.cosmeticId( inventoryCosmeticId( inventory ) );
        cosmeticInventoryResponse.cosmeticName( inventoryCosmeticName( inventory ) );
        cosmeticInventoryResponse.manufacturer( inventoryCosmeticManufacturer( inventory ) );
        cosmeticInventoryResponse.id( inventory.getId() );
        cosmeticInventoryResponse.batchCode( inventory.getBatchCode() );
        cosmeticInventoryResponse.quantity( inventory.getQuantity() );
        cosmeticInventoryResponse.expiryDate( inventory.getExpiryDate() );
        cosmeticInventoryResponse.status( inventory.getStatus() );

        return cosmeticInventoryResponse.build();
    }

    private String inventoryCosmeticId(CosmeticInventory cosmeticInventory) {
        if ( cosmeticInventory == null ) {
            return null;
        }
        Cosmetic cosmetic = cosmeticInventory.getCosmetic();
        if ( cosmetic == null ) {
            return null;
        }
        String id = cosmetic.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String inventoryCosmeticName(CosmeticInventory cosmeticInventory) {
        if ( cosmeticInventory == null ) {
            return null;
        }
        Cosmetic cosmetic = cosmeticInventory.getCosmetic();
        if ( cosmetic == null ) {
            return null;
        }
        String name = cosmetic.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String inventoryCosmeticManufacturer(CosmeticInventory cosmeticInventory) {
        if ( cosmeticInventory == null ) {
            return null;
        }
        Cosmetic cosmetic = cosmeticInventory.getCosmetic();
        if ( cosmetic == null ) {
            return null;
        }
        String manufacturer = cosmetic.getManufacturer();
        if ( manufacturer == null ) {
            return null;
        }
        return manufacturer;
    }
}
