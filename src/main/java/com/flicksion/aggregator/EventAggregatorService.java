package com.flicksion.aggregator;

import com.flicksion.aggregator.forumcinemas.ForumCinemasEvent;
import com.flicksion.aggregator.forumcinemas.ForumCinemasRepository;
import com.flicksion.aggregator.omdb.OmdbFindMoviesResponse;
import com.flicksion.aggregator.omdb.OmdbMovie;
import com.flicksion.aggregator.omdb.OmdbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.disjoint;
import static java.util.stream.Collectors.toList;

@Service
public class EventAggregatorService {

    private final ForumCinemasRepository forumCinemasRepository;

    private final OmdbRepository omdbRepository;

    @Autowired
    public EventAggregatorService(ForumCinemasRepository forumCinemasRepository,
                                  OmdbRepository omdbRepository) {
        this.forumCinemasRepository = forumCinemasRepository;
        this.omdbRepository = omdbRepository;
    }

    public List<Event> filterEventsByActors(List<String> actors) {
        List<Event> aggregatedResults = this.getAggregatedEvents();

        return aggregatedResults.stream()
                .filter(entry -> !disjoint(entry.getActors(), actors))
                .collect(toList());
    }

    public List<Event> getAggregatedEvents() {
        Map<Event, List<OmdbMovie>> eventsWithSearchResults = this
                .getEventsWithSearchResults();

        return eventsWithSearchResults.entrySet().stream()
                .map(entry -> {
                    Event event = entry.getKey();
                    List<OmdbMovie> searchResults = entry.getValue();

                    // TODO: Match search results by more relevant criteria
                    List<OmdbMovie> matchingMovies = searchResults.stream()
                            .filter(movie -> event.getYear().equals(movie.getYear()))
                            .collect(toList());

                    if (matchingMovies.size() < 1) {
                        return event;
                    }

                    OmdbMovie firstMatchingMovie = matchingMovies.get(0);

                    // TODO: Aggregate more info from omdb entity into event
                    event.setActors(firstMatchingMovie.getActors());

                    return event;
                })
                .collect(toList());
    }

    // TODO: Get events from more sources
    public Map<Event, List<OmdbMovie>> getEventsWithSearchResults() {
        List<ForumCinemasEvent> forumCinemasEvents = forumCinemasRepository.getEvents();

        Map<Event, List<OmdbMovie>> eventsSearchResults = new HashMap<>();
        forumCinemasEvents.stream()
                .filter(event -> "Movie".equals(event.getEventType()))
                .forEach(forumCinemasEvent -> {
                    OmdbFindMoviesResponse response = omdbRepository
                            .findMovies(forumCinemasEvent.getOriginalTitle());

                    List<OmdbMovie> fullMovies = new ArrayList<>();
                    if (response.getSearch() != null) {
                        fullMovies = response.getSearch().stream()
                                .map(shortOmdbMovie -> omdbRepository.findMovie(shortOmdbMovie.getImdbID()))
                                .collect(toList());
                    }

                    Event event = Event.newBuilder()
                            .originalTitle(forumCinemasEvent.getOriginalTitle())
                            .year(forumCinemasEvent.getProductionYear())
                            .build();

                    eventsSearchResults.put(event, fullMovies);
                });

        return eventsSearchResults;
    }
}
