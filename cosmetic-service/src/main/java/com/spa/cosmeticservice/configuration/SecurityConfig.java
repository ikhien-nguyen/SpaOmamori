package com.spa.cosmeticservice.configuration;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.regex.Pattern;

/**
 * RequestMatcher that combines HTTP method check with UUID pattern matching.
 */
class HttpMethodUuidRequestMatcher implements RequestMatcher {
    private final HttpMethod httpMethod;
    private final Pattern uuidPattern;

    public HttpMethodUuidRequestMatcher(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
        // UUID pattern: 550e8400-e29b-41d4-a716-446655440000
        this.uuidPattern = Pattern.compile(
                "^/([0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12})$");
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        if (httpMethod != null && httpMethod != HttpMethod.valueOf(request.getMethod())) {
            return false;
        }
        String path = request.getServletPath();
        return uuidPattern.matcher(path).matches();
    }
}

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // Within servlet context /cosmetics, controllers are mapped to:
    //
    // CosmeticController (base path /):
    //   GET /         -> getAll()
    //   GET /{id}     -> getById(id) where id is UUID
    //   POST /        -> create()
    //   PUT /{id}     -> update(id)
    //
    // CosmeticInventoryController (base path /inventory):
    //   GET /inventory         -> getAll()
    //   POST /inventory        -> stockIn()
    //   PUT /inventory/{id}   -> update(id)
    //   GET /inventory/stats   -> getStats()
    //
    // CosmeticOrderController (base path /cosmetic-orders):
    //   POST /cosmetic-orders                -> create()
    //   GET /cosmetic-orders/{id}            -> getById(id)
    //   GET /cosmetic-orders/appointment/{appointmentId} -> getByAppointment(appointmentId)
    //
    // Public catalog endpoints: GET / and GET /{UUID} (CosmeticController only)
    // All other paths require authentication.

    private final CustomJwtDecoder customJwtDecoder;

    public SecurityConfig(CustomJwtDecoder customJwtDecoder) {
        this.customJwtDecoder = customJwtDecoder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        RequestMatcher uuidDetailMatcher = new HttpMethodUuidRequestMatcher(HttpMethod.GET);

        httpSecurity.authorizeHttpRequests(request -> request
                // Public catalog: GET / (root) and GET /{UUID} (detail)
                // UUID pattern ensures only valid cosmetic IDs are publicly accessible
                .requestMatchers(HttpMethod.GET, "/").permitAll()
                .requestMatchers(uuidDetailMatcher).permitAll()
                // All other paths require authentication
                // This explicitly includes: /inventory/**, /cosmetic-orders/**, POST, PUT, DELETE
                .anyRequest().authenticated());

        httpSecurity.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer
                        .decoder(customJwtDecoder)
                        .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint()));
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }
}
