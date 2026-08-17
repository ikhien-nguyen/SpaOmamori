package com.spa.userservice.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.spa.userservice.client.ProfileClient;
import com.spa.userservice.dto.request.CreateProfileRequest;
import com.spa.userservice.dto.request.TherapistCreationRequest;
import com.spa.userservice.dto.request.UserCreationRequest;
import com.spa.userservice.dto.response.UserCreationResponse;
import com.spa.userservice.dto.response.UserResponse;
import com.spa.userservice.entity.Role;
import com.spa.userservice.entity.User;
import com.spa.userservice.exception.AppException;
import com.spa.userservice.exception.ErrorCode;
import com.spa.userservice.mapper.UserMapper;
import com.spa.userservice.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    ProfileClient profileClient;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;
    public UserCreationResponse createUser(UserCreationRequest userCreationRequest) {
        if (userRepository.existsByEmail(userCreationRequest.getEmail())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.toUser(userCreationRequest);
        user.setPassword(passwordEncoder.encode(userCreationRequest.getPassword()));
        user.setRole(Role.CUSTOMER);
        user.setActive(true);
        userRepository.save(user);

        try {
            CreateProfileRequest profileRequest = CreateProfileRequest.builder()
                    .userId(user.getId())
                    .fullName(user.getFullName())
                    .role(user.getRole().name())
                    .dateOfBirth(userCreationRequest.getDateOfBirth())
                    .gender(userCreationRequest.getGender())
                    .phone(userCreationRequest.getPhone())
                    .address(userCreationRequest.getAddress())
                    .build();

            profileClient.createProfile(profileRequest);
        } catch (Exception ex) {
            log.error("Tạo profile thất bại cho userId={}, rollback User. Lỗi: {}",
                    user.getId(), ex.getMessage());
            userRepository.deleteById(user.getId());
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        UserCreationResponse response = userMapper.toUserResponse(user);
        // Không trả password (dù đã hash) về client — tránh rò rỉ dữ liệu nhạy cảm.
        response.setPassword(null);
        return response;
    }

    /**
     * UC_09 - Admin tạo tài khoản Kỹ thuật viên (STAFF) hoặc Admin khác.
     * Khác createUser(): role do Admin chỉ định thay vì hard-code CUSTOMER.
     */
    public UserCreationResponse createStaffUser(TherapistCreationRequest request) {
        Role role;
        try {
            role = Role.valueOf(request.getRole());
        } catch (IllegalArgumentException ex) {
            throw new AppException(ErrorCode.INVALID_ROLE);
        }
        if (role == Role.CUSTOMER) {
            // Khách hàng phải tự đăng ký qua /auth/create, Admin không tạo hộ CUSTOMER ở đây.
            throw new AppException(ErrorCode.INVALID_ROLE);
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        user.setActive(true);
        userRepository.save(user);

        try {
            CreateProfileRequest profileRequest = CreateProfileRequest.builder()
                    .userId(user.getId())
                    .fullName(user.getFullName())
                    .role(user.getRole().name())
                    .dateOfBirth(request.getDateOfBirth())
                    .gender(request.getGender())
                    .phone(request.getPhone())
                    .address(request.getAddress())
                    .build();

            profileClient.createProfile(profileRequest);
        } catch (Exception ex) {
            log.error("Tạo profile thất bại cho userId={}, rollback User. Lỗi: {}",
                    user.getId(), ex.getMessage());
            userRepository.deleteById(user.getId());
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        UserCreationResponse response = userMapper.toUserResponse(user);
        response.setPassword(null);
        return response;
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserListItem)
                .toList();
    }

    public UserResponse getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserListItem(user);
    }

    /**
     * UC_09 - Admin khóa/mở khóa tài khoản. active=false chặn đăng nhập ở login().
     */
    public UserResponse setUserActive(String id, boolean active) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        user.setActive(active);
        userRepository.save(user);
        return userMapper.toUserListItem(user);
    }

    public String login(String email, String rawPassword) {
        var userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) throw new AppException(ErrorCode.UNAUTHENTICATED);
        User user = userOpt.get();

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        if (!user.isActive()) {
            throw new AppException(ErrorCode.USER_LOCKED);
        }

        return generateToken(user);
    }
    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getId())
                .issuer("Omamori.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", user.getRole())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = (isRefresh)
                ? new Date(signedJWT
                .getJWTClaimsSet()
                .getIssueTime()
                .toInstant()
                .plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS)
                .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if (!(verified && expiryTime.after(new Date()))) throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }
}