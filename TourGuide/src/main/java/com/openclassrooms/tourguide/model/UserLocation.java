package com.openclassrooms.tourguide.model;

import java.util.List;
import java.util.UUID;

import gpsUtil.location.Location;

public class UserLocation {
    private final UUID userId;
    private final String username;
    private final Location userLocation;
    private final List<NearByAttraction> nearByAttractions;

    public UserLocation(UUID userId, String username, Location userLocation, List<NearByAttraction> nearByAttractions) {
        this.userId = userId;
        this.username = username;
        this.userLocation = userLocation;
        this.nearByAttractions = nearByAttractions;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public Location getUserLocation() {
        return userLocation;
    }

    public List<NearByAttraction> getNearByAttractions() {
        return nearByAttractions;
    }
}
