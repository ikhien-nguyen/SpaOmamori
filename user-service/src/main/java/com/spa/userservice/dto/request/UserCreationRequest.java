package com.spa.userservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Past;
import lombok.*;

import java.time.LocalDate;

/**
 * Dữ liệu đầu vào cho use case "Đăng ký tài khoản".
 * Chỉ áp dụng cho Customer tự đăng ký (Therapist/Admin do Admin tạo qua kênh riêng).
 *
 * Request này gộp cả thông tin xác thực (thuộc USERS) lẫn thông tin hồ sơ
 * (thuộc PROFILES bên Profile Service) để người dùng chỉ cần điền 1 lần duy nhất.
 * User Service sẽ tách 2 phần này ra khi xử lý — xem AuthService#register().
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreationRequest {

    // ----- Phần thuộc bảng USERS (User Service) -----

    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{6,}$",
            message = "Mật khẩu tối thiểu 6 ký tự, gồm ít nhất 1 chữ hoa, 1 chữ thường và 1 chữ số"
    )
    private String password;

    // ----- Phần thuộc bảng PROFILES (Profile Service) -----

    @NotNull(message = "Ngày sinh không được để trống")
    @Past(message = "Ngày sinh phải là một ngày trong quá khứ")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Giới tính không được để trống")
    @Pattern(regexp = "MALE|FEMALE|OTHER", message = "Giới tính không hợp lệ")
    private String gender;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(0|\\+84)[0-9]{9,10}$", message = "Số điện thoại không đúng định dạng")
    private String phone;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;
}
