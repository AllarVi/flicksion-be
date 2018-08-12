package com.flicksion.omdb;


import com.fasterxml.jackson.annotation.JsonProperty;

public class ShortOmdbMovie {

    @JsonProperty("Title")
    private String title;

    @JsonProperty("Year")
    private Integer year;

    @JsonProperty("imdbID")
    private String imdbID;

    @JsonProperty("Type")
    private String type;

    @JsonProperty("Poster")
    private String poster;

    public ShortOmdbMovie() {
    }

    public ShortOmdbMovie(Builder builder) {
        this.title = builder.title;
        this.year = builder.year;
        this.imdbID = builder.imdbID;
        this.type = builder.type;
        this.poster = builder.poster;
    }

    String getTitle() {
        return title;
    }

    Integer getYear() {
        return year;
    }

    public String getImdbID() {
        return imdbID;
    }

    String getType() {
        return type;
    }

    String getPoster() {
        return poster;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String title;
        private Integer year;
        private String imdbID;
        private String type;
        private String poster;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder year(Integer year) {
            this.year = year;
            return this;
        }

        public Builder imdbID(String imdbID) {
            this.imdbID = imdbID;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder poster(String poster) {
            this.poster = poster;
            return this;
        }

        public ShortOmdbMovie build() {
            return new ShortOmdbMovie(this);
        }
    }
}
