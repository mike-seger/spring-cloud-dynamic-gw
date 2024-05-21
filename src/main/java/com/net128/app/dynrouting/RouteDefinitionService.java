package com.net128.app.dynrouting;

import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;

@Service
public class RouteDefinitionService {

    private final InMemoryRouteDefinitionRepository routeDefinitionRepository;
    private final ApplicationEventPublisher publisher;

    public RouteDefinitionService(InMemoryRouteDefinitionRepository routeDefinitionRepository, ApplicationEventPublisher publisher) {
        this.routeDefinitionRepository = routeDefinitionRepository;
        this.publisher = publisher;
    }

    public Flux<RouteDefinition> getRoutes() {
        return routeDefinitionRepository.getRouteDefinitions()
            .sort(Comparator.comparing(RouteDefinition::getId));
    }

    public Flux<Void> setRoutes(List<RouteDefinition> routeDefinitions) {
        return Flux.fromIterable(routeDefinitions)
                .flatMap(this::setRoute);
    }

    public Mono<Void> setRoute(RouteDefinition routeDefinition) {
        return routeDefinitionRepository.save(Mono.just(routeDefinition)).then(Mono.defer(() -> {
            publisher.publishEvent(new RefreshRoutesEvent(this));
            return Mono.empty();
        }));
    }

    public Mono<Void> deleteRoute(String routeId) {
        return routeDefinitionRepository.delete(Mono.just(routeId)).then(Mono.defer(() -> {
            publisher.publishEvent(new RefreshRoutesEvent(this));
            return Mono.empty();
        }));
    }
}
