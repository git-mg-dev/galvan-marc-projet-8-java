package com.openclassrooms.tourguide.model;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;

public class UserReward {

	private final VisitedLocation visitedLocation;
	private final Attraction attraction;
	private int rewardPoints;

	public UserReward(VisitedLocation visitedLocation, Attraction attraction, int rewardPoints) {
		this.visitedLocation = visitedLocation;
		this.attraction = attraction;
		this.rewardPoints = rewardPoints;
	}

	public VisitedLocation getVisitedLocation() { return visitedLocation; }

	public Attraction getAttraction() { return attraction; }

	public int getRewardPoints() {
		return rewardPoints;
	}

	public void setRewardPoints(int rewardPoints) {
		this.rewardPoints = rewardPoints;
	}
}
