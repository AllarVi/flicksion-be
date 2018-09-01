package com.flicksion.flick.aggregator;

import java.util.ArrayList;
import java.util.List;

public class Event {

    private String originalTitle;
    private List<String> actors;
    private String year;

    public Event() {
    }

    public Event(Builder builder) {
        this.originalTitle = builder.originalTitle;
        this.actors = builder.actors;
        this.year = builder.year;
    }

    public List<String> getActors() {
        return actors;
    }

    public void setActors(List<String> actors) {
        this.actors = actors;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getYear() {
        return year;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String originalTitle;
        private List<String> actors = new ArrayList<>();
        private String year;

        private Builder() {
        }

        public Builder originalTitle(String originalTitle) {
            this.originalTitle = originalTitle;
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

        public Event build() {
            return new Event(this);
        }
    }

    @Override
    public String toString() {
        return "Event{" +
                "originalTitle='" + originalTitle + '\'' +
                '}';
    }
}
