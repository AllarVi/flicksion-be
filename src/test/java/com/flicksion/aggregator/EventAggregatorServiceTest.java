package com.flicksion.aggregator;

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
                        .eventType("Movie")
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

        when(omdbRepository.findMovies("Batman Begins")).thenReturn(OmdbFindMoviesResponse.newBuilder()
                .search(shortOmdbMovies)
                .build());

        OmdbMovie omdbMovie = OmdbMovie.newBuilder()
                .actors(singletonList("Henry Cavill"))
                .build();

        when(omdbRepository.findMovie("imdbId1")).thenReturn(omdbMovie);
    }

    @Test
    public void shouldFetchMoviesFromCinema() {
        eventAggregatorService.getEventsWithSearchResults();

        verify(forumCinemasRepository, times(1)).getEvents();
    }

    @Test
    public void shouldContainEvents() {
        eventAggregatorService.getEventsWithSearchResults();

        verify(omdbRepository, times(1)).findMovies("Batman Begins");
    }

    @Test
    public void shouldFetchFullMovies() {
        eventAggregatorService.getEventsWithSearchResults();

        verify(omdbRepository, times(1)).findMovie("imdbId1");
        verify(omdbRepository, times(1)).findMovie("imdbId2");
    }

    @Test
    public void shouldReturnAggregatedResults() {
        Map<Event, List<OmdbMovie>> results = eventAggregatorService
                .getEventsWithSearchResults();

        assertEquals(1, results.size());
        results.forEach((key, value) -> {
            if (key.getOriginalTitle().equals("Superman")) {
                assertEquals(singletonList("Henry Cavill"), value.get(0).getActors());
            }
        });
    }

    @Test
    public void shouldFetchMoviesWhenFilteringByActors() {
        List<Event> aggregatedResults = asList(
                Event.newBuilder()
                        .originalTitle("Man of Steel")
                        .actors(singletonList("Henry Cavill"))
                        .build(),
                Event.newBuilder()
                        .originalTitle("Da Vinci Code")
                        .actors(singletonList("Tom Hanks"))
                        .build()
        );

        EventAggregatorService eventAggregatorServiceMock = mock(EventAggregatorService.class);
        when(eventAggregatorServiceMock.filterEventsByActors(any())).thenCallRealMethod();
        doReturn(aggregatedResults).when(eventAggregatorServiceMock).getAggregatedEvents();

        List<Event> movies = eventAggregatorServiceMock
                .filterEventsByActors(asList("Tom Hanks", "Madonna"));

        verify(eventAggregatorServiceMock, times(1))
                .getAggregatedEvents();

        assertEquals(1, movies.size());
        assertEquals("Da Vinci Code", movies.get(0).getOriginalTitle());
    }

    @Test
    public void shouldAggregateEventWithCorrespondingSearchResult() {
        Map<Event, List<OmdbMovie>> eventsWithSearchResults = new HashMap<Event, List<OmdbMovie>>() {
            {
                put(Event.newBuilder()
                                .originalTitle("Superman")
                                .year("2013")
                                .build(),
                        asList(
                                OmdbMovie.newBuilder()
                                        .actors(singletonList("Henry Cavill"))
                                        .year("2013")
                                        .build(),
                                OmdbMovie.newBuilder()
                                        .actors(singletonList("Tom Hanks"))
                                        .year("2006")
                                        .build()));
            }
        };

        EventAggregatorService eventAggregatorServiceMock = mock(EventAggregatorService.class);
        when(eventAggregatorServiceMock.getAggregatedEvents()).thenCallRealMethod();
        doReturn(eventsWithSearchResults).when(eventAggregatorServiceMock)
                .getEventsWithSearchResults();

        List<Event> events = eventAggregatorServiceMock
                .getAggregatedEvents();

        verify(eventAggregatorServiceMock, times(1))
                .getEventsWithSearchResults();

        assertEquals(singletonList("Henry Cavill"), events.get(0).getActors());
        assertEquals("Superman", events.get(0).getOriginalTitle());
        assertEquals("2013", events.get(0).getYear());

    }
}
