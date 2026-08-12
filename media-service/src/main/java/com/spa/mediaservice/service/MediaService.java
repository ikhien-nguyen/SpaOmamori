package com.spa.mediaservice.service;

import com.spa.mediaservice.config.FileStorageConfig;
import com.spa.mediaservice.dto.response.MediaUploadResponse;
import com.spa.mediaservice.exception.AppException;
import com.spa.mediaservice.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Không có Repository/Entity: Media Service chỉ ghi file lên đĩa và trả về URL,
 * không lưu bất kỳ metadata nào (ai upload, thuộc về entity nào...) — trách nhiệm
 * đó thuộc về service gọi nó (Profile Service, Treatment Service...), theo đúng
 * quyết định thiết kế của nhóm.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MediaService {

    FileStorageConfig fileStorageConfig;

    public MediaUploadResponse upload(MultipartFile file) {
        validate(file);

        String extension = extractExtension(file.getOriginalFilename());
        // UUID làm tên file để tránh trùng tên/đè file và tránh path traversal
        // từ originalFilename do client gửi lên (không dùng originalFilename trực tiếp).
        String storedFileName = UUID.randomUUID() + extension;
        Path targetPath = fileStorageConfig.getStoragePath().resolve(storedFileName).normalize();

        try {
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("Lưu file thất bại: {}", e.getMessage());
            throw new AppException(ErrorCode.FILE_STORAGE_FAILED);
        }

        return MediaUploadResponse.builder()
                .fileName(storedFileName)
                .url(fileStorageConfig.getPublicBaseUrl() + "/" + storedFileName)
                .contentType(file.getContentType())
                .sizeInBytes(file.getSize())
                .build();
    }

    public Resource load(String fileName) {
        try {
            Path filePath = fileStorageConfig.getStoragePath().resolve(fileName).normalize();

            // Chặn path traversal (VD: fileName = "../../etc/passwd") — file thực tế
            // phải nằm đúng trong thư mục lưu trữ đã cấu hình.
            if (!filePath.getParent().equals(fileStorageConfig.getStoragePath())) {
                throw new AppException(ErrorCode.FILE_NOT_FOUND);
            }

            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new AppException(ErrorCode.FILE_NOT_FOUND);
            }

            return resource;
        } catch (MalformedURLException e) {
            throw new AppException(ErrorCode.FILE_NOT_FOUND);
        }
    }

    public void delete(String fileName) {
        Path filePath = fileStorageConfig.getStoragePath().resolve(fileName).normalize();

        if (!filePath.getParent().equals(fileStorageConfig.getStoragePath())) {
            throw new AppException(ErrorCode.FILE_NOT_FOUND);
        }

        try {
            boolean deleted = Files.deleteIfExists(filePath);
            if (!deleted) {
                throw new AppException(ErrorCode.FILE_NOT_FOUND);
            }
        } catch (IOException e) {
            log.error("Xóa file thất bại: {}", e.getMessage());
            throw new AppException(ErrorCode.FILE_STORAGE_FAILED);
        }
    }

    private void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new AppException(ErrorCode.FILE_EMPTY);
        }

        String contentType = file.getContentType();
        if (contentType == null || !fileStorageConfig.getAllowedContentTypes().contains(contentType)) {
            throw new AppException(ErrorCode.FILE_TYPE_NOT_SUPPORTED);
        }
    }

    private String extractExtension(String originalFileName) {
        if (originalFileName == null || !originalFileName.contains(".")) {
            return "";
        }
        return originalFileName.substring(originalFileName.lastIndexOf("."));
    }
}
