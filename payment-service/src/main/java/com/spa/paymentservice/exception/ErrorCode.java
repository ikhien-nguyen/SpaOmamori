package com.spa.paymentservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    INVOICE_NOT_EXISTED(1050, "Không tìm thấy hóa đơn", HttpStatus.NOT_FOUND),
    INVOICE_EMPTY(1051, "Hóa đơn phải có ít nhất 1 dịch vụ hoặc 1 sản phẩm mỹ phẩm", HttpStatus.BAD_REQUEST),
    INVOICE_ALREADY_EXISTS_FOR_APPOINTMENT(1052, "Lịch hẹn này đã được lập hóa đơn", HttpStatus.BAD_REQUEST),
    INVOICE_NOT_PENDING(1053, "Hóa đơn không ở trạng thái Chờ thanh toán", HttpStatus.BAD_REQUEST),
    APPOINTMENT_NOT_FOUND(1054, "Không tìm thấy lịch hẹn", HttpStatus.NOT_FOUND),
    APPOINTMENT_NOT_COMPLETED(1055, "Lịch hẹn chưa hoàn thành, không thể lập hóa đơn", HttpStatus.BAD_REQUEST),
    VNPAY_INVOICE_NOT_PENDING(1056, "Hóa đơn không ở trạng thái chờ thanh toán, không thể tạo lại giao dịch VNPay", HttpStatus.BAD_REQUEST),
    COSMETIC_NOT_EXISTED(1057, "Không tìm thấy sản phẩm mỹ phẩm", HttpStatus.NOT_FOUND),
    COSMETIC_ID_REQUIRED(1058, "Mỗi dòng mỹ phẩm trong hóa đơn phải gắn với một sản phẩm trong danh mục", HttpStatus.BAD_REQUEST),
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
