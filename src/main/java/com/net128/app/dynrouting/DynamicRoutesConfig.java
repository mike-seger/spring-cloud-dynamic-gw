package com.net128.app.dynrouting;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "dynamic-routes")
public class DynamicRoutesConfig {
    private List<RouteDefinition> routes;
}
