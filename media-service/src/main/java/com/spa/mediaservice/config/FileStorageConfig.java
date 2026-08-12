package com.spa.mediaservice.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Đọc cấu hình lưu trữ file từ application.yaml (media.storage.location,
 * media.public-base-url, media.allowed-content-types) và tạo sẵn thư mục lưu
 * file lúc service khởi động, để MediaService không phải tự check/tạo mỗi lần upload.
 */
@Configuration
@Slf4j
@Getter
public class FileStorageConfig {

    @Value("${media.storage.location}")
    private String storageLocation;

    @Value("${media.public-base-url}")
    private String publicBaseUrl;

    @Value("${media.allowed-content-types}")
    private String allowedContentTypesRaw;

    private Path storagePath;
    private List<String> allowedContentTypes;

    @PostConstruct
    public void init() {
        storagePath = Paths.get(storageLocation).toAbsolutePath().normalize();
        allowedContentTypes = Arrays.asList(allowedContentTypesRaw.split(","));

        try {
            Files.createDirectories(storagePath);
        } catch (IOException e) {
            // Lỗi ở bước này là lỗi cấu hình môi trường (sai quyền ghi, sai path...),
            // nên fail-fast ngay lúc khởi động thay vì để lỗi rơi vào lúc upload đầu tiên.
            throw new IllegalStateException("Không thể tạo thư mục lưu trữ media: " + storagePath, e);
        }

        log.info("Media Service lưu file tại: {}", storagePath);
    }
}
