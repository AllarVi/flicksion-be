package com.flicksion.flick.aggregator.omdb;

import com.flicksion.flick.AggregatorTestConfiguration;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@RestClientTest(OmdbRepository.class)
@ContextConfiguration(classes = AggregatorTestConfiguration.class)
public class OmdbRepositoryTest {

    @Autowired
    private MockRestServiceServer mockServer;

    @Autowired
    private OmdbRepository omdbRepository;

    @Test
    public void shouldFindMovies() throws Exception {
        mockServer.expect(requestTo("http://www.omdbapi.com/?s=hot%20summer%20nights&apikey=8d7caf3c"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(getResource("getMoviesResponse.json"), MediaType.APPLICATION_JSON));

        OmdbFindMoviesResponse result = omdbRepository.findMovies("hot summer nights");

        mockServer.verify();
        assertEquals(2, result.getSearch().size());
        assertEquals(true, result.getResponse());
        assertEquals(2, result.getTotalResults());

        ShortOmdbMovie shortOmdbMovie1 = result.getSearch().get(0);
        assertEquals("Hot Summer Nights", shortOmdbMovie1.getTitle());
        assertEquals("2017", shortOmdbMovie1.getYear());
        assertEquals("tt3416536", shortOmdbMovie1.getImdbID());
        assertEquals("movie", shortOmdbMovie1.getType());
        assertEquals("https://m.media-amazon.com/images/M/MV5BYWEzZDI3NTQtYmFlZi00N2QxLTk0MmYtMThjYjlmZmRlMmVjXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_SX300.jpg", shortOmdbMovie1.getPoster());
    }

    @Test
    public void shouldFindMovieByImdbID() throws Exception {
        mockServer.expect(requestTo("http://www.omdbapi.com/?i=tt3416536&apikey=8d7caf3c"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(getResource("getMovieByImdbID.json"), MediaType.APPLICATION_JSON));

        OmdbMovie result = omdbRepository.findMovie("tt3416536");

        mockServer.verify();

        assertEquals("tt3416536", result.getImdbID());
        assertEquals("Hot Summer Nights", result.getTitle());
        List<String> expected = asList("Timothée Chalamet", "Maika Monroe", "Alex Roe", "Emory Cohen");
        assertEquals(expected, result.getActors());
    }

    private String getResource(String fileName) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(fileName);

        return FileUtils.readFileToString(classPathResource.getFile(), StandardCharsets.UTF_8);
    }
}
