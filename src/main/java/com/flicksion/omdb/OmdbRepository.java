package com.flicksion.omdb;

import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;

import static java.util.Arrays.asList;

@Repository
public class OmdbRepository {

    public OmdbFindMoviesResponse findMovies(String searchQuery) {

        String requestUri = UriComponentsBuilder.fromHttpUrl("http://www.omdbapi.com/")
                .queryParam("s", searchQuery)
                .queryParam("apikey","8d7caf3c")
                .toUriString();

        RestTemplate restTemplate = new RestTemplate();
        OmdbFindMoviesResponse omdbFindMoviesResponse = restTemplate.getForObject(requestUri, OmdbFindMoviesResponse.class, 200);

        return omdbFindMoviesResponse;
    }
}
