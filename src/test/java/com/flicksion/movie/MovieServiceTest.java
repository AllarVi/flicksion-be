package com.flicksion.movie;

import com.flicksion.omdb.OmdbFindMoviesResponse;
import com.flicksion.omdb.OmdbMovie;
import com.flicksion.omdb.OmdbRepository;
import com.flicksion.omdb.ShortOmdbMovie;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class MovieServiceTest {

    @Mock
    private ForumCinemasRepository forumCinemasRepository;

    @Mock
    private OmdbRepository omdbRepository;

    private MovieService movieService;

    @Before
    public void setUp() {
        movieService = new MovieService(forumCinemasRepository, omdbRepository);

        List<Event> events = asList(
                Event.newBuilder()
                        .id("id1")
                        .originalTitle("Batman Begins")
                        .build(),
                Event.newBuilder()
                        .id("id2")
                        .originalTitle("Superman")
                        .build()
        );

        when(forumCinemasRepository.getEvents()).thenReturn(events);

        List<ShortOmdbMovie> shortOmdbMovies = asList(
                ShortOmdbMovie.newBuilder()
                        .imdbID("imdbId1")
                        .build(),
                ShortOmdbMovie.newBuilder()
                        .imdbID("imdbId2")
                        .build()
        );

        when(omdbRepository.findMovies("Superman")).thenReturn(OmdbFindMoviesResponse.newBuilder()
                .search(shortOmdbMovies)
                .build());

        when(omdbRepository.findMovies("Batman Begins")).thenReturn(OmdbFindMoviesResponse.newBuilder()
                .search(emptyList())
                .build());

        OmdbMovie omdbMovie = OmdbMovie.newBuilder()
                .actors("Henry Cavill")
                .build();

        when(omdbRepository.findMovie("imdbId1")).thenReturn(omdbMovie);
    }

    @Test
    public void shouldFetchMoviesFromCinema() {
        movieService.getAggregatedEventsWithSearchResults();

        verify(forumCinemasRepository, times(1)).getEvents();
    }

    @Test
    public void shouldContainEvents() {
        movieService.getAggregatedEventsWithSearchResults();

        verify(omdbRepository, times(1)).findMovies("Superman");
        verify(omdbRepository, times(1)).findMovies("Batman Begins");
    }

    @Test
    public void shouldFetchFullMovies() {
        movieService.getAggregatedEventsWithSearchResults();

        verify(omdbRepository, times(1)).findMovie("imdbId1");
        verify(omdbRepository, times(1)).findMovie("imdbId2");
    }

    @Test
    public void shouldReturnAggregatedResults() {
        Map<String, List<OmdbMovie>> results = movieService
                .getAggregatedEventsWithSearchResults();

        assertEquals(2, results.size());
        assertEquals(2, results.get("id2").size());
        assertEquals("Henry Cavill", results.get("id2").get(0).getActors());
    }

    @Test
    public void shouldFetchMoviesWhenFilteringByActors() {
        Map<String, List<OmdbMovie>> aggregatedResults = new HashMap<String, List<OmdbMovie>>() {
            {
                put("eventId100", singletonList(OmdbMovie.newBuilder()
                        .actors("Henry Cavill")
                        .build()));
                put("eventId101", singletonList(OmdbMovie.newBuilder()
                        .actors("Tom Hanks")
                        .build()));
            }
        };

        MovieService movieServiceMock = mock(MovieService.class);
        when(movieServiceMock.filterEventsByActors(any())).thenCallRealMethod();
        doReturn(aggregatedResults).when(movieServiceMock).getAggregatedEventsWithSearchResults();

        List<String> movies = movieServiceMock
                .filterEventsByActors(asList("Tom Hanks", "Madonna"));

        verify(movieServiceMock, times(1))
                .getAggregatedEventsWithSearchResults();

        assertEquals(1, movies.size());
        assertEquals("eventId101", movies.get(0));
    }

}
