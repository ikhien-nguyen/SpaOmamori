package com.spa.mediaservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "Người dùng đã tồn tại", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Sai tài khoản hoặc mật khẩu", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL(1009, "Invalid email address", HttpStatus.BAD_REQUEST),
    EMAIL_IS_REQUIRED(1009, "Email is required", HttpStatus.BAD_REQUEST),
    SERVICE_NOT_EXISTED(1020, "Không tìm thấy dịch vụ", HttpStatus.NOT_FOUND),
    APPOINTMENT_NOT_EXISTED(1021, "Không tìm thấy lịch hẹn", HttpStatus.NOT_FOUND),
    APPOINTMENT_SLOT_TAKEN(1022, "Kỹ thuật viên đã có lịch hẹn khác vào thời gian này", HttpStatus.BAD_REQUEST),
    THERAPY_RECORD_EXISTED(1023, "Lịch hẹn này đã có nhật ký trị liệu", HttpStatus.BAD_REQUEST),
    INVALID_APPOINTMENT_STATUS_TRANSITION(1024, "Không thể chuyển sang trạng thái này", HttpStatus.BAD_REQUEST),
    FILE_EMPTY(1030, "File tải lên không được để trống", HttpStatus.BAD_REQUEST),
    FILE_TYPE_NOT_SUPPORTED(1031, "Định dạng file không được hỗ trợ, chỉ chấp nhận JPEG/PNG/WEBP", HttpStatus.BAD_REQUEST),
    FILE_TOO_LARGE(1032, "Kích thước file vượt quá giới hạn cho phép (5MB)", HttpStatus.BAD_REQUEST),
    FILE_NOT_FOUND(1033, "Không tìm thấy file", HttpStatus.NOT_FOUND),
    FILE_STORAGE_FAILED(1034, "Lưu file thất bại, vui lòng thử lại", HttpStatus.INTERNAL_SERVER_ERROR),
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
