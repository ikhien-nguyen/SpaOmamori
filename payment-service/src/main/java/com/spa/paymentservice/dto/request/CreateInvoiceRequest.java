package com.spa.paymentservice.dto.request;

import jakarta.validation.Valid;
import lombok.*;

import java.util.List;

/**
 * UC_11 - dieu kien dac biet: "Hoa don tao ra phai gan dung Ma khach hang va
 * Ma lich hen (neu co)". Co 2 truong hop:
 *  - appointmentId co gia tri: hoa don dich vu, customerId se LAY TU chinh
 *    appointment (khong tin tuong customerId client tu truyen len, tranh gan
 *    nham/gia mao) - xem InvoiceService.
 *  - appointmentId = null: hoa don ban le my pham, bat buoc phai co
 *    customerId + cosmeticItems.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateInvoiceRequest {

    private String appointmentId;

    // Bat buoc khi appointmentId = null (ban le). Neu co appointmentId thi
    // gia tri nay se bi bo qua, lay lai tu Appointment Service cho chac chan.
    private String customerId;

    @Valid
    private List<InvoiceItemRequest> cosmeticItems;

    // Giup validate 2 truong hop tren o tang DTO, tranh phai check tay
    // nhieu lan ben Service. Khong danh dau @AssertTrue truc tiep tren field
    // boolean vi Lombok @Data da sinh isValid() trung ten - dat method rieng
    // va goi thu cong trong Service.
    public boolean isRetailSale() {
        return appointmentId == null || appointmentId.isBlank();
    }
}
