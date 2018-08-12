package com.flicksion.omdb;


import com.fasterxml.jackson.annotation.JsonProperty;

class ShortOmdbMovie {

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

    String getTitle() {
        return title;
    }

    Integer getYear() {
        return year;
    }

    String getImdbID() {
        return imdbID;
    }

    String getType() {
        return type;
    }

    String getPoster() {
        return poster;
    }
}
