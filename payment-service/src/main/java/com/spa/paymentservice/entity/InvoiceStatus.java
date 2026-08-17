package com.spa.paymentservice.entity;

/**
 * UC_11: "Chờ thanh toán" khi mới lập hóa đơn, chuyển "Đã thanh toán" khi
 * Admin xác nhận. CANCELLED dùng khi Admin hủy hóa đơn lập nhầm (chưa thu
 * tiền) - không có trong luồng chính UC_11 nhưng là thao tác hợp lý cần có.
 */
public enum InvoiceStatus {
    PENDING_PAYMENT,
    PAID,
    CANCELLED,
}
