package com.openclassrooms.tourguide.model;

import java.util.UUID;

public class NearByAttraction {
    private final UUID attractionId;
    private final String name;
    private final double longitude;
    private final double latitude;
    private final double distanceFromUser;
    private final int rewardPoints;

    public NearByAttraction(Attraction attraction, Double distanceFromUser, int rewardPoints) {
        this.attractionId = attraction.getAttractionId();
        this.name = attraction.getAttractionName();
        this.longitude = attraction.getLongitude();
        this.latitude = attraction.getLatitude();
        this.distanceFromUser = distanceFromUser;
        this.rewardPoints = rewardPoints;
    }

    public UUID getAttractionId() {
        return attractionId;
    }

    public String getName() {
        return name;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getDistanceFromUser() {
        return distanceFromUser;
    }

    public int getRewardPoints() {
        return rewardPoints;
    }
}
