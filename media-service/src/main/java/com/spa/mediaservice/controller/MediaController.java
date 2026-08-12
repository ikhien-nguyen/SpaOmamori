package com.spa.mediaservice.controller;

import com.spa.mediaservice.dto.response.ApiResponse;
import com.spa.mediaservice.dto.response.MediaUploadResponse;
import com.spa.mediaservice.service.MediaService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Khác với các service khác (User, Profile), Media Service là public-facing:
 * Frontend upload ảnh trực tiếp lên đây (VD: chọn ảnh đại diện) để lấy về url,
 * sau đó mới gửi url đó kèm trong request sang service tương ứng (VD:
 * UpdateProfileRequest.avatarUrl). Vì vậy KHÔNG đặt dưới prefix "/internal"
 * và CẦN khai báo route cho "/media/**" ở API Gateway.
 */
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MediaController {

    MediaService mediaService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<MediaUploadResponse> upload(@RequestParam("file") MultipartFile file) {
        return ApiResponse.<MediaUploadResponse>builder()
                .result(mediaService.upload(file))
                .build();
    }

    @GetMapping("/files/{fileName}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileName) {
        Resource resource = mediaService.load(fileName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @DeleteMapping("/{fileName}")
    public ApiResponse<Void> delete(@PathVariable String fileName) {
        mediaService.delete(fileName);

        return ApiResponse.<Void>builder()
                .message("Xóa file thành công")
                .build();
    }
}
