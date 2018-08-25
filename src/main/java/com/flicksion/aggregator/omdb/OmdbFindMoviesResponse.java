package com.flicksion.aggregator.omdb;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class OmdbFindMoviesResponse {

    @JsonProperty("Search")
    private List<ShortOmdbMovie> search;

    @JsonProperty("totalResults")
    private int totalResults;

    @JsonProperty("Response")
    private Boolean response;

    public OmdbFindMoviesResponse() {
    }

    public OmdbFindMoviesResponse(Builder builder) {
        this.search = builder.search;
        this.totalResults = builder.totalResults;
        this.response = builder.response;
    }

    public List<ShortOmdbMovie> getSearch() {
        return search;
    }

    int getTotalResults() {
        return totalResults;
    }

    Boolean getResponse() {
        return response;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private List<ShortOmdbMovie> search;
        private int totalResults;
        private Boolean response;

        public Builder search(List<ShortOmdbMovie> search) {
            this.search = search;
            return this;
        }

        public Builder totalResults(int totalResults) {
            this.totalResults = totalResults;
            return this;
        }

        public Builder response(Boolean response) {
            this.response = response;
            return this;
        }

        public OmdbFindMoviesResponse build() {
            return new OmdbFindMoviesResponse(this);
        }
    }
}
