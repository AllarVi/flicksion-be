package com.flicksion.aggregator.omdb;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import static java.util.Arrays.asList;

public class OmdbMovie {

    @JsonProperty("imdbID")
    private String imdbID;

    @JsonProperty("Title")
    private String title;

    @JsonProperty("Actors")
    private List<String> actors;

    @JsonProperty("Year")
    private String year;

    public OmdbMovie() {
    }

    public OmdbMovie(Builder builder) {
        this.imdbID = builder.imdbID;
        this.title = builder.title;
        this.actors = builder.actors;
        this.year = builder.year;
    }

    public String getImdbID() {
        return imdbID;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getActors() {
        return actors;
    }

    public List<String> setActors(String actors) {
        return this.actors = asList(actors.trim().split("\\s*,\\s*"));
    }

    public String getYear() {
        return year;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String imdbID;
        private String title;
        private List<String> actors;
        private String year;

        public Builder imdbID(String imdbID) {
            this.imdbID = imdbID;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder actors(List<String> actors) {
            this.actors = actors;
            return this;
        }

        public Builder year(String year) {
            this.year = year;
            return this;
        }

        public OmdbMovie build() {
            return new OmdbMovie(this);
        }
    }
}
