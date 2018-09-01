package com.flicksion.flick.greeting;

import com.flicksion.flick.aggregator.EventAggregatorService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.Set;

import static org.hibernate.validator.internal.util.CollectionHelper.asSet;

@Component
public class EventsHandler {

    private final EventAggregatorService eventAggregatorService;

    public EventsHandler(EventAggregatorService eventAggregatorService) {
        this.eventAggregatorService = eventAggregatorService;
    }

    Mono<ServerResponse> getEvents(ServerRequest request) {
        Optional<String> searchResultsParam = request.queryParam("searchResults");
        boolean searchResults = searchResultsParam.map(Boolean::valueOf).orElse(false);

        // TODO: For debugging, remove later
        if (searchResults) {
            return ServerResponse
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromObject(eventAggregatorService.getEventsWithSearchResults()));
        }

        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(eventAggregatorService.getEvents()));
    }

    Mono<ServerResponse> getEventsByActors(ServerRequest request) {
        String actorsNames = request.pathVariable("actorsNames");
        Set<String> actors = asSet(actorsNames.split(","));

        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(eventAggregatorService.getEventsByActors(actors)));
    }
}
