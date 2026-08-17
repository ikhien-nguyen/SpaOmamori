package com.spa.notificationservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    NOTIFICATION_NOT_FOUND(1080, "Không tìm thấy thông báo", HttpStatus.NOT_FOUND),
    NOTIFICATION_ACCESS_DENIED(1081, "Bạn không có quyền truy cập thông báo này", HttpStatus.FORBIDDEN),
    EMAIL_REQUIRED_FOR_CHANNEL(1082, "recipientEmail bắt buộc khi channel = EMAIL", HttpStatus.BAD_REQUEST),
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
