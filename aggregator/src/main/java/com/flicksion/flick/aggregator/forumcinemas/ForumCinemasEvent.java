package com.flicksion.flick.aggregator.forumcinemas;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "Events")
public class ForumCinemasEvent {

    @JacksonXmlProperty(localName = "ID")
    private String id;
    @JacksonXmlProperty(localName = "Title")
    private String title;
    @JacksonXmlProperty(localName = "OriginalTitle")
    private String originalTitle;
    @JacksonXmlProperty(localName = "ProductionYear")
    private String productionYear;
    @JacksonXmlProperty(localName = "EventType")
    private String eventType;

    public ForumCinemasEvent() {
    }

    public ForumCinemasEvent(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.originalTitle = builder.originalTitle;
        this.productionYear = builder.productionYear;
        this.eventType = builder.eventType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getProductionYear() {
        return productionYear;
    }

    public String getEventType() {
        return eventType;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String id;
        private String title;
        private String originalTitle;
        private String productionYear;
        private String eventType;

        private Builder() {
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder originalTitle(String originalTitle) {
            this.originalTitle = originalTitle;
            return this;
        }

        public Builder productionYear(String productionYear) {
            this.productionYear = productionYear;
            return this;
        }

        public Builder eventType(String eventType) {
            this.eventType = eventType;
            return this;
        }

        public ForumCinemasEvent build() {
            return new ForumCinemasEvent(this);
        }
    }
}
