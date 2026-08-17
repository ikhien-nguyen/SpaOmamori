package com.spa.cosmeticservice.mapper;

import com.spa.cosmeticservice.dto.response.CosmeticPrescriptionResponse;
import com.spa.cosmeticservice.entity.Cosmetic;
import com.spa.cosmeticservice.entity.CosmeticPrescription;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-17T20:06:08+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23-valhalla (Oracle Corporation)"
)
@Component
public class CosmeticPrescriptionMapperImpl implements CosmeticPrescriptionMapper {

    @Override
    public CosmeticPrescriptionResponse toResponse(CosmeticPrescription prescription) {
        if ( prescription == null ) {
            return null;
        }

        CosmeticPrescriptionResponse.CosmeticPrescriptionResponseBuilder cosmeticPrescriptionResponse = CosmeticPrescriptionResponse.builder();

        cosmeticPrescriptionResponse.cosmeticId( prescriptionCosmeticId( prescription ) );
        cosmeticPrescriptionResponse.cosmeticName( prescriptionCosmeticName( prescription ) );
        cosmeticPrescriptionResponse.id( prescription.getId() );
        cosmeticPrescriptionResponse.appointmentId( prescription.getAppointmentId() );
        cosmeticPrescriptionResponse.technicianId( prescription.getTechnicianId() );
        cosmeticPrescriptionResponse.quantity( prescription.getQuantity() );
        cosmeticPrescriptionResponse.unitPrice( prescription.getUnitPrice() );
        cosmeticPrescriptionResponse.prescribedAt( prescription.getPrescribedAt() );

        return cosmeticPrescriptionResponse.build();
    }

    private String prescriptionCosmeticId(CosmeticPrescription cosmeticPrescription) {
        if ( cosmeticPrescription == null ) {
            return null;
        }
        Cosmetic cosmetic = cosmeticPrescription.getCosmetic();
        if ( cosmetic == null ) {
            return null;
        }
        String id = cosmetic.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String prescriptionCosmeticName(CosmeticPrescription cosmeticPrescription) {
        if ( cosmeticPrescription == null ) {
            return null;
        }
        Cosmetic cosmetic = cosmeticPrescription.getCosmetic();
        if ( cosmetic == null ) {
            return null;
        }
        String name = cosmetic.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
