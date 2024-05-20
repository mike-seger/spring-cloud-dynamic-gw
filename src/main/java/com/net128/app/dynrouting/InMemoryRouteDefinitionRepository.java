package com.net128.app.dynrouting;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class InMemoryRouteDefinitionRepository implements RouteDefinitionRepository {
    private final DynamicRoutesConfig dynamicRoutesConfig;

    @PostConstruct
    public void init() {
        // Load the dynamic routes from the configuration
        for (RouteDefinition routeDefinition : dynamicRoutesConfig.getRoutes()) {
            routes.put(routeDefinition.getId(), routeDefinition);
        }
    }

    private final Map<String, RouteDefinition> routes = new HashMap<>();

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        return Flux.fromIterable(routes.values());
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return route.doOnNext(r -> routes.put(r.getId(), r)).then();
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return routeId.doOnNext(routes::remove).then();
    }
}
