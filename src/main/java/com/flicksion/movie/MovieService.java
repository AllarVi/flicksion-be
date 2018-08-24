package com.flicksion.movie;

import com.flicksion.omdb.OmdbFindMoviesResponse;
import com.flicksion.omdb.OmdbMovie;
import com.flicksion.omdb.OmdbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
public class MovieService {

    private final ForumCinemasRepository forumCinemasRepository;

    private final OmdbRepository omdbRepository;

    @Autowired
    public MovieService(ForumCinemasRepository forumCinemasRepository,
                        OmdbRepository omdbRepository) {
        this.forumCinemasRepository = forumCinemasRepository;
        this.omdbRepository = omdbRepository;
    }

    public List<String> filterEventsByActors(List<String> actors) {
        Map<String, List<OmdbMovie>> aggregatedResults = this
                .getAggregatedEventsWithSearchResults();

        return aggregatedResults.entrySet().stream()
                .filter(entry -> {
                    List<String> actorsForMovie = actors.stream().filter(actor -> {
                        List<OmdbMovie> moviesForActor = entry.getValue().stream()
                                .filter(omdbMovie ->
                                        omdbMovie.getActors().contains(actor)
                                )
                                .collect(toList());
                        return moviesForActor.size() != 0;
                    }).collect(toList());

                    return actorsForMovie.size() != 0;
                })
                .map(Map.Entry::getKey)
                .collect(toList());
    }

    public Map<String, List<OmdbMovie>> getAggregatedEventsWithSearchResults() {
        List<Event> events = forumCinemasRepository.getEvents();

        Map<String, List<OmdbMovie>> aggregatedResults = new HashMap<>();
        events.forEach(event -> {
            OmdbFindMoviesResponse response = omdbRepository
                    .findMovies(event.getOriginalTitle());

            List<OmdbMovie> fullMovies = new ArrayList<>();
            if (response.getSearch() != null) {
                fullMovies = response.getSearch().stream()
                        .map(shortOmdbMovie -> omdbRepository.findMovie(shortOmdbMovie.getImdbID()))
                        .collect(toList());
            }

            aggregatedResults.put(event.getId(), fullMovies);
        });

        return aggregatedResults;
    }
}
