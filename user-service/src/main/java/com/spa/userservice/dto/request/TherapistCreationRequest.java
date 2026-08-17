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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TherapistCreationRequest {

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

    /**
     * ADMIN có thể tạo THERAPIST hoặc ADMIN.
     */
    @NotBlank(message = "Vai trò không được để trống")
    @Pattern(
            regexp = "THERAPIST|ADMIN",
            message = "Vai trò không hợp lệ, chỉ chấp nhận THERAPIST hoặc ADMIN"
    )
    private String role;

    @NotNull(message = "Ngày sinh không được để trống")
    @Past(message = "Ngày sinh phải là một ngày trong quá khứ")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Giới tính không được để trống")
    @Pattern(
            regexp = "MALE|FEMALE|OTHER",
            message = "Giới tính không hợp lệ"
    )
    private String gender;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(
            regexp = "^(0|\\+84)[0-9]{9,10}$",
            message = "Số điện thoại không đúng định dạng"
    )
    private String phone;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;
}