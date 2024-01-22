package com.openclassrooms.tourguide.model;

import java.util.UUID;

public class Attraction extends Location{

    private final String attractionName;
    private final String city;
    private final String state;
    private final UUID attractionId;

    public Attraction(String attractionName, String city, String state, double latitude, double longitude) {
        super(latitude, longitude);
        this.attractionName = attractionName;
        this.city = city;
        this.state = state;
        this.attractionId = UUID.randomUUID();
    }

    public String getAttractionName() {
        return attractionName;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public UUID getAttractionId() {
        return attractionId;
    }
}
