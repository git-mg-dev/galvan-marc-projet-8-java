package com.openclassrooms.tourguide.model;

import java.util.Date;
import java.util.UUID;

public class VisitedLocation {
    private final UUID userId;
    private final Location location;
    public final Date timeVisited;

    public VisitedLocation(UUID userId, Location location, Date timeVisited) {
        this.userId = userId;
        this.location = location;
        this.timeVisited = timeVisited;
    }

    public UUID getUserId() {
        return userId;
    }

    public Location getLocation() {
        return location;
    }

    public Date getTimeVisited() {
        return timeVisited;
    }
}
