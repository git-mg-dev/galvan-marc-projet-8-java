package com.openclassrooms.tourguide.model;

import java.util.UUID;

public class Provider {
    private final String name;
    private final double price;
    private final UUID tripId;

    public Provider(UUID tripId, String name, double price) {
        this.name = name;
        this.tripId = tripId;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public UUID getTripId() {
        return tripId;
    }
}
