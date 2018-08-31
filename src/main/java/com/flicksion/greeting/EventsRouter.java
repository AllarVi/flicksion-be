package com.flicksion.greeting;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class EventsRouter {

    @Bean
    public RouterFunction<ServerResponse> route(EventsHandler eventsHandler) {
        return RouterFunctions
                .route(GET("/events")
                                .and(accept(MediaType.TEXT_PLAIN)),
                        eventsHandler::getEvents)
                .andRoute(GET("/events/actors/{actorsNames}")
                                .and(accept(MediaType.TEXT_PLAIN)),
                        eventsHandler::getEventsByActors);
    }
}
