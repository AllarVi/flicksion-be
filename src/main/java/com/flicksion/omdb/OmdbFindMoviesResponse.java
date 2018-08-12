package com.flicksion.omdb;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

class OmdbFindMoviesResponse {

    @JsonProperty("Search")
    private List<ShortOmdbMovie> search;

    @JsonProperty("totalResults")
    private int totalResults;

    @JsonProperty("Response")
    private Boolean response;

    List<ShortOmdbMovie> getSearch() {
        return search;
    }

    int getTotalResults() {
        return totalResults;
    }

    Boolean getResponse() {
        return response;
    }
}
