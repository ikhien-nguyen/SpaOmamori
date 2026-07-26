package com.spa.userservice.mapper;

import com.spa.userservice.dto.request.UserCreationRequest;
import com.spa.userservice.dto.response.UserCreationResponse;
import com.spa.userservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Ánh xạ giữa User entity và các DTO của User Service.
 *
 * Lưu ý:
 * - id: do DB tự sinh (UUID) -> luôn ignore khi tạo mới.
 * - password: KHÔNG map trực tiếp từ request sang entity ở đây, vì cần mã hóa
 *   bằng BCryptPasswordEncoder trước khi lưu. Việc set password đã hash được
 *   thực hiện thủ công trong Service, sau khi gọi toUser().
 * - role: mặc định gán CUSTOMER trong Service khi tự đăng ký, nên ignore ở mapper
 *   để tránh client tự truyền role tùy ý qua request (rủi ro bảo mật).
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    User toUser(UserCreationRequest request);

    UserCreationResponse toUserResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    void updateUser(@MappingTarget User user, UserCreationRequest request);
}