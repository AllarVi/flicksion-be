package com.flicksion.greeting;

import com.flicksion.aggregator.EventAggregatorService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static java.util.Collections.singletonList;

@Component
public class GreetingHandler {

    private final EventAggregatorService eventAggregatorService;

    public GreetingHandler(EventAggregatorService eventAggregatorService) {
        this.eventAggregatorService = eventAggregatorService;
    }

    Mono<ServerResponse> hello(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(eventAggregatorService
                        .getAggregatedEvents()));
    }
}
