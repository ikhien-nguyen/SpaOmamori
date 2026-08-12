package com.spa.mediaservice.dto.response;

import lombok.*;

/**
 * Trả về cho caller (Frontend hoặc service khác gọi Feign) sau khi upload thành công.
 * fileName là "khóa" duy nhất để gọi lại GET/DELETE sau này; url là đường dẫn public
 * đầy đủ để hiển thị ảnh (VD: gắn thẳng vào thẻ <img src>).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaUploadResponse {
    private String fileName;
    private String url;
    private String contentType;
    private long sizeInBytes;
}
