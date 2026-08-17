package com.spa.paymentservice.dto.request;

import com.spa.paymentservice.entity.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmPaymentRequest {

    @NotNull(message = "Vui lòng chọn hình thức thanh toán")
    private PaymentMethod paymentMethod;
}
