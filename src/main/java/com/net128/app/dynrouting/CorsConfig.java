package com.net128.app.dynrouting;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfigurationSource source = new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(ServerWebExchange exchange) {
                CorsConfiguration corsConfig = new CorsConfiguration();
                String origin = exchange.getRequest().getHeaders().getOrigin();
                if (isAllowedOrigin(origin)) {
                    corsConfig.addAllowedOrigin(origin);
                    corsConfig.addAllowedMethod("*");
                    corsConfig.addAllowedHeader("*");
                    corsConfig.setAllowCredentials(true);
                }
                return corsConfig;
            }

            private boolean isAllowedOrigin(String origin) {
                return origin != null && (origin.matches("http://localhost(:\\d+)?") || origin.matches("http://127.0.0.1(:\\d+)?"));
            }
        };

        return new CorsWebFilter(source);
    }
}
