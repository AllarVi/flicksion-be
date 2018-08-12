package com.flicksion.omdb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Repository
public class OmdbRepository {

    private final RestTemplate restTemplate;

    @Autowired
    public OmdbRepository(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    OmdbFindMoviesResponse findMovies(String searchQuery) {

        URI requestUri = UriComponentsBuilder.fromHttpUrl("http://www.omdbapi.com/")
                .queryParam("s", searchQuery)
                .queryParam("apikey", "8d7caf3c")
                .build()
                .toUri();

        return restTemplate.getForObject(requestUri, OmdbFindMoviesResponse.class);
    }
}
