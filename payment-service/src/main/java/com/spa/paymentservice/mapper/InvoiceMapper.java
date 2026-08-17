package com.spa.paymentservice.mapper;

import com.spa.paymentservice.dto.response.InvoiceItemResponse;
import com.spa.paymentservice.dto.response.InvoiceResponse;
import com.spa.paymentservice.entity.Invoice;
import com.spa.paymentservice.entity.InvoiceItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {

    InvoiceResponse toInvoiceResponse(Invoice invoice);

    InvoiceItemResponse toInvoiceItemResponse(InvoiceItem item);
}
