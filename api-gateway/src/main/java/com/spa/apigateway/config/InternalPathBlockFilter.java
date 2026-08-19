package com.spa.apigateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

/**
 * Blocks any request that targets a service-to-service "/internal/**" endpoint
 * via the public API Gateway. These endpoints are intended for direct
 * Feign/Eureka calls between services (e.g. appointment-service ->
 * /treatments/internal/{id} via TreatmentClient at http://localhost:8084).
 *
 * The path of every Gateway route begins with ${app.api-prefix} (default
 * "/api/omamori"). Any path matching "/api/omamori/{service}/internal/..."
 * is short-circuited to 404 — no downstream forwarding, no auth check.
 *
 * Only Gateway prefixes that ACTUALLY expose a /internal/** controller are
 * listed (verified against each service's controllers). Routes that have no
 * /internal/** surface (e.g. cosmetics, payments, notifications, users,
 * media) are intentionally excluded so we don't shadow legitimate public
 * paths or 404 healthy traffic.
 */
@Component
@org.springframework.core.annotation.Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class InternalPathBlockFilter implements GlobalFilter {

    private static final Pattern INTERNAL_PATH =
            Pattern.compile("^/api/omamori/(profiles|treatments|rooms|appointments)/internal(/.*)?$");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        if (INTERNAL_PATH.matcher(path).matches()) {
            log.warn("Blocked access to internal endpoint via Gateway: {}", path);
            exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
            byte[] body = ("{\"code\":1404,\"message\":\"Endpoint not found\"}")
                    .getBytes(StandardCharsets.UTF_8);
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(body);
            return exchange.getResponse().writeWith(Mono.just(buffer));
        }
        return chain.filter(exchange);
    }
}
