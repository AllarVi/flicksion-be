package com.flicksion.aggregator;

import com.flicksion.aggregator.EventAggregatorService;
import com.flicksion.aggregator.forumcinemas.ForumCinemasEvent;
import com.flicksion.aggregator.forumcinemas.ForumCinemasRepository;
import com.flicksion.aggregator.omdb.OmdbFindMoviesResponse;
import com.flicksion.aggregator.omdb.OmdbMovie;
import com.flicksion.aggregator.omdb.OmdbRepository;
import com.flicksion.aggregator.omdb.ShortOmdbMovie;
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
public class EventAggregatorServiceTest {

    @Mock
    private ForumCinemasRepository forumCinemasRepository;

    @Mock
    private OmdbRepository omdbRepository;

    private EventAggregatorService eventAggregatorService;

    @Before
    public void setUp() {
        eventAggregatorService = new EventAggregatorService(forumCinemasRepository, omdbRepository);

        List<ForumCinemasEvent> events = asList(
                ForumCinemasEvent.newBuilder()
                        .id("id1")
                        .originalTitle("Batman Begins")
                        .build(),
                ForumCinemasEvent.newBuilder()
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
        eventAggregatorService.getAggregatedEventsWithSearchResults();

        verify(forumCinemasRepository, times(1)).getEvents();
    }

    @Test
    public void shouldContainEvents() {
        eventAggregatorService.getAggregatedEventsWithSearchResults();

        verify(omdbRepository, times(1)).findMovies("Superman");
        verify(omdbRepository, times(1)).findMovies("Batman Begins");
    }

    @Test
    public void shouldFetchFullMovies() {
        eventAggregatorService.getAggregatedEventsWithSearchResults();

        verify(omdbRepository, times(1)).findMovie("imdbId1");
        verify(omdbRepository, times(1)).findMovie("imdbId2");
    }

    @Test
    public void shouldReturnAggregatedResults() {
        Map<String, List<OmdbMovie>> results = eventAggregatorService
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

        EventAggregatorService eventAggregatorServiceMock = mock(EventAggregatorService.class);
        when(eventAggregatorServiceMock.filterEventsByActors(any())).thenCallRealMethod();
        doReturn(aggregatedResults).when(eventAggregatorServiceMock).getAggregatedEventsWithSearchResults();

        List<String> movies = eventAggregatorServiceMock
                .filterEventsByActors(asList("Tom Hanks", "Madonna"));

        verify(eventAggregatorServiceMock, times(1))
                .getAggregatedEventsWithSearchResults();

        assertEquals(1, movies.size());
        assertEquals("eventId101", movies.get(0));
    }

}
