package com.spa.apigateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Bắt mọi exception xảy ra trong Gateway (route không tồn tại, downstream service
 * không phản hồi kịp timeout, connection refused...) và trả về JSON theo cùng
 * format ApiResponse{code, message} mà user-service/profile-service đang dùng,
 * thay vì trang lỗi Whitelabel mặc định của Spring.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class GlobalErrorWebExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        log.error("Gateway error at path {}: {}", exchange.getRequest().getPath(), ex.getMessage());

        HttpStatus status = resolveStatus(ex);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("code", status.value() == 503 ? 9998 : 9999);
        body.put("message", resolveMessage(status));

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        DataBuffer buffer;
        try {
            buffer = exchange.getResponse().bufferFactory()
                    .wrap(objectMapper.writeValueAsBytes(body));
        } catch (Exception e) {
            buffer = exchange.getResponse().bufferFactory()
                    .wrap("{\"code\":9999,\"message\":\"Uncategorized error\"}"
                            .getBytes(StandardCharsets.UTF_8));
        }

        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    private HttpStatus resolveStatus(Throwable ex) {
        if (ex instanceof java.util.concurrent.TimeoutException
                || ex.getClass().getSimpleName().contains("Timeout")) {
            return HttpStatus.GATEWAY_TIMEOUT;
        }
        if (ex instanceof java.net.ConnectException
                || ex.getMessage() != null && ex.getMessage().contains("Connection refused")) {
            return HttpStatus.SERVICE_UNAVAILABLE;
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private String resolveMessage(HttpStatus status) {
        return switch (status) {
            case GATEWAY_TIMEOUT -> "Dịch vụ phản hồi quá chậm, vui lòng thử lại";
            case SERVICE_UNAVAILABLE -> "Dịch vụ hiện không khả dụng, vui lòng thử lại sau";
            default -> "Đã xảy ra lỗi hệ thống";
        };
    }
}