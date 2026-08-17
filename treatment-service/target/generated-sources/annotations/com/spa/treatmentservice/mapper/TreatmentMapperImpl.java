package com.spa.treatmentservice.mapper;

import com.spa.treatmentservice.dto.request.TreatmentCreationRequest;
import com.spa.treatmentservice.dto.response.TreatmentResponse;
import com.spa.treatmentservice.entity.Treatment;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-17T20:07:31+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23-valhalla (Oracle Corporation)"
)
@Component
public class TreatmentMapperImpl implements TreatmentMapper {

    @Override
    public Treatment toTreatment(TreatmentCreationRequest request) {
        if ( request == null ) {
            return null;
        }

        Treatment.TreatmentBuilder treatment = Treatment.builder();

        treatment.name( request.getName() );
        treatment.category( request.getCategory() );
        treatment.price( request.getPrice() );
        treatment.durationMinutes( request.getDurationMinutes() );
        treatment.description( request.getDescription() );

        return treatment.build();
    }

    @Override
    public TreatmentResponse toTreatmentResponse(Treatment treatment) {
        if ( treatment == null ) {
            return null;
        }

        TreatmentResponse.TreatmentResponseBuilder treatmentResponse = TreatmentResponse.builder();

        treatmentResponse.id( treatment.getId() );
        treatmentResponse.name( treatment.getName() );
        treatmentResponse.category( treatment.getCategory() );
        treatmentResponse.price( treatment.getPrice() );
        treatmentResponse.durationMinutes( treatment.getDurationMinutes() );
        treatmentResponse.description( treatment.getDescription() );
        treatmentResponse.isActive( treatment.getIsActive() );

        return treatmentResponse.build();
    }
}
