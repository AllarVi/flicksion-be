package com.flicksion.aggregator.forumcinemas;

import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

@Repository
public class ForumCinemasRepository {

    public List<ForumCinemasEvent> getEvents() {
        RestTemplate restTemplate = new RestTemplate();
        ForumCinemasEvent[] events = restTemplate.getForObject("https://www.forumcinemas.ee/xml/Events", ForumCinemasEvent[].class, 200);

        return new ArrayList<>(asList(events));
    }
}
