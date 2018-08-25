package com.flicksion.aggregator.omdb;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OmdbMovie {

    @JsonProperty("imdbID")
    private String imdbID;

    @JsonProperty("Title")
    private String title;

    @JsonProperty("Actors")
    private String actors;

    public OmdbMovie() {
    }

    public OmdbMovie(Builder builder) {
        this.imdbID = builder.imdbID;
        this.title = builder.title;
        this.actors = builder.actors;
    }

    public String getImdbID() {
        return imdbID;
    }

    public String getTitle() {
        return title;
    }

    public String getActors() {
        return actors;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String imdbID;
        private String title;
        private String actors;

        public Builder imdbID(String imdbID) {
            this.imdbID = imdbID;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder actors(String actors) {
            this.actors = actors;
            return this;
        }

        public OmdbMovie build() {
            return new OmdbMovie(this);
        }
    }
}
