package com.spa.userservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Dữ liệu đầu vào cho use case "Quản lý người dùng & nhân sự" (UC_09) — chỉ Admin
 * mới được gọi endpoint dùng DTO này, để tạo tài khoản Kỹ thuật viên (STAFF) hoặc
 * Admin khác. Khác với UserCreationRequest (tự đăng ký, luôn hard-code CUSTOMER),
 * DTO này cho phép Admin chỉ định role tường minh.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffCreationRequest {

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

    // Chỉ chấp nhận STAFF hoặc ADMIN — validate thêm ở Service vì @Pattern trên enum
    // string không đủ rõ ràng; xem UserService#createStaffUser().
    @NotBlank(message = "Vai trò không được để trống")
    @Pattern(regexp = "STAFF|ADMIN", message = "Vai trò không hợp lệ, chỉ chấp nhận STAFF hoặc ADMIN")
    private String role;

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
