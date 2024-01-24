package com.openclassrooms.tourguide.model;

import java.util.UUID;
import gpsUtil.location.Attraction;

public class NearByAttraction {
    private final UUID attractionId;
    private final String name;
    private final double longitude;
    private final double latitude;
    private final double distanceFromUser;
    private final int rewardPoints;

    public NearByAttraction(Attraction attraction, Double distanceFromUser, int rewardPoints) {
        this.attractionId = attraction.attractionId;
        this.name = attraction.attractionName;
        this.longitude = attraction.longitude;
        this.latitude = attraction.latitude;
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
