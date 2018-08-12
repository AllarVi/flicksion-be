package com.flicksion.movie;

import com.flicksion.omdb.OmdbFindMoviesResponse;
import com.flicksion.omdb.OmdbMovie;
import com.flicksion.omdb.OmdbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
                        .collect(Collectors.toList());
            }

            aggregatedResults.put(event.getId(), fullMovies);
        });

        return aggregatedResults;
    }
}
