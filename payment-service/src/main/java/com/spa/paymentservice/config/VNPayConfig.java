package com.spa.paymentservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Binding tu prefix "vnpay" trong application.yaml. tmnCode/hashSecret la
 * thong tin merchant sandbox - dang ky tai
 * https://sandbox.vnpayment.vn/devreg/ de lay, KHONG duoc commit gia tri
 * that len git cong khai.
 */
@Component
@ConfigurationProperties(prefix = "vnpay")
@Getter
@Setter
public class VNPayConfig {
    private String tmnCode;
    private String hashSecret;
    private String payUrl;
    private String returnUrl;
}
