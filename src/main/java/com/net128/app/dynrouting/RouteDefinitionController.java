package com.net128.app.dynrouting;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Tag(name = "Dynamic Route Definition API", description =
    """
        This implementation of dynamic route definitions is for testing purposes only!!
        It has no persistent storage backing the added route definitions, but is kept in memory.
        All definitions will be lost, when the gateway shuts down.
        This API doesn't manage the default routes configured under spring.cloud.gateway.routes.
    """)
@RestController
@RequestMapping("/routes")
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class RouteDefinitionController {
    private final RouteDefinitionService routeDefinitionService;

    @GetMapping
    @Operation(summary = "Get all route definitions.")
    public Flux<RouteDefinition> getRoutes() {
        return routeDefinitionService.getRoutes();
    }

    @PatchMapping
    @Operation(summary = "Set a single route definition.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                schema = @Schema(implementation = RouteDefinition.class),
                examples = { @ExampleObject(value = exampleRouteDefinition) }))
    )
    public Mono<ResponseEntity<Void>> setRoute(@RequestBody RouteDefinition routeDefinition) {
        return routeDefinitionService.setRoute(routeDefinition)
            .then(Mono.just(ResponseEntity.ok().build()));
    }

    @PostMapping
    @Operation(summary = "Set all route definitions.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                schema = @Schema(implementation = RouteDefinitionList.class),
                examples = { @ExampleObject(value = "[\n"+exampleRouteDefinition+"\n]" ) }))
    )
    public Mono<ResponseEntity<Void>> setRoutes(@RequestBody List<RouteDefinition> routeDefinitions) {
        return Mono.just(routeDefinitions)
            .flatMapMany(routeDefinitionService::setRoutes)
            .then(Mono.just(ResponseEntity.ok().build()));
    }

    @DeleteMapping("/{routeId}")
    @Operation(summary = "Delete a single route definition.",
            parameters = {@Parameter(name = "routeId", example = "microservice2-route")})
    public Mono<ResponseEntity<Void>> deleteRoute(@PathVariable String routeId) {
        return routeDefinitionService.deleteRoute(routeId)
            .then(Mono.just(ResponseEntity.ok().build()));
    }

    @DeleteMapping("")
    @Operation(summary = "Delete all route definitions.",
            parameters = {@Parameter(name = "routeId", example = "microservice2-route")})
    public Mono<ResponseEntity<Void>> deleteAllRoutes(@PathVariable String routeId) {
        return routeDefinitionService.deleteAllRoutes()
            .then(Mono.just(ResponseEntity.ok().build()));
    }


    @Data
    private static class RouteDefinitionList {
        private List<RouteDefinition> routeDefinitions;
    }

    private final static String exampleRouteDefinition = """
      {
        "id": "microservice3-route",
        "predicates": [
          {
            "name": "Path",
            "args": {
              "0": "/microservice3/**"
            }
          }
        ],
        "filters": [
          {
            "name": "StripPrefix",
            "args": {
              "0": "1"
            }
          }
        ],
        "uri": "http://localhost:10003",
        "metadata": {},
        "order": 0
      }
      """;
}
