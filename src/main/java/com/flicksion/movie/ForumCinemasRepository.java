package com.flicksion.movie;

import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

@Repository
public class ForumCinemasRepository {

    public List<Event> getEvents() {
        RestTemplate restTemplate = new RestTemplate();
        Event[] events = restTemplate.getForObject("https://www.forumcinemas.ee/xml/Events", Event[].class, 200);

        return new ArrayList<>(asList(events));
    }
}
