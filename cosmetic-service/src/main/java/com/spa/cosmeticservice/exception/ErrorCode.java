package com.spa.cosmeticservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    COSMETIC_NOT_FOUND(1040, "Không tìm thấy mỹ phẩm", HttpStatus.NOT_FOUND),
    INVENTORY_NOT_FOUND(1041, "Không tìm thấy lô hàng tồn kho", HttpStatus.NOT_FOUND),
    INVENTORY_NOT_ENOUGH(1042, "Số lượng tồn kho không đủ", HttpStatus.BAD_REQUEST),
    PRESCRIPTION_NOT_FOUND(1043, "Không tìm thấy đơn kê mỹ phẩm", HttpStatus.NOT_FOUND),
    INVALID_EXPIRY_DATE(1044, "Ngày hết hạn phải sau ngày hiện tại", HttpStatus.BAD_REQUEST),
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
