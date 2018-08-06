package com.flicksion.omdb;

import org.junit.Before;
import org.junit.Test;
import org.apache.commons.io.FileUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@RestClientTest(OmdbRepository.class)
@AutoConfigureWebClient
@ContextConfiguration(classes = OmdbTestConfiguration.class)
public class OmdbRepositoryTest {

    private MockRestServiceServer mockServer;

    @Autowired
    private OmdbRepository omdbRepository;

    @Before
    public void setUp(){
        mockServer = MockRestServiceServer.createServer(new RestTemplate());
    }

    @Test
    public void findMovies() throws Exception {
        mockServer.expect(requestTo("http://www.omdbapi.com/?s=hot%20summer%20nights&apikey=8d7caf3c"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess(getResource("getMoviesResponse.json"), MediaType.APPLICATION_JSON));

        OmdbFindMoviesResponse result = omdbRepository.findMovies("hot summer nights");

        mockServer.verify();
    }

    protected String getResource(String fileName) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(fileName);

        return FileUtils.readFileToString(classPathResource.getFile(), StandardCharsets.UTF_8);
    }
}