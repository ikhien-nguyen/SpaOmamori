package com.spa.paymentservice.dto.response;

import lombok.*;

/**
 * paymentUrl la link VNPay dua ve - FE redirect trinh duyet toi day (hoac mo
 * webview). Trang VNPay tu hien thi ca 2 lua chon cho khach: quet ma QR
 * (VNPAY-QR/app ngan hang) hoac nhap the/tai khoan - khong can tu ve QR o
 * phia minh.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VnPayPaymentUrlResponse {
    private String paymentUrl;
    private String txnRef;
}
