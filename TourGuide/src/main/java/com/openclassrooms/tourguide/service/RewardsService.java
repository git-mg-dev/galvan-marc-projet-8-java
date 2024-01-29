package com.openclassrooms.tourguide.service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.springframework.stereotype.Service;

import rewardCentral.RewardCentral;
import com.openclassrooms.tourguide.model.User;
import com.openclassrooms.tourguide.model.UserReward;

@Service
public class RewardsService {
    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

	// proximity in miles
    private final int defaultProximityBuffer = 10;
	private int proximityBuffer = defaultProximityBuffer;
	private int attractionProximityRange = 200;
	private final GpsUtil gpsUtil;
	private final RewardCentral rewardsCentral;
	private ExecutorService executorService = Executors.newFixedThreadPool(1000);

	public RewardsService(GpsUtil gpsUtil, RewardCentral rewardCentral) {
		this.gpsUtil = gpsUtil;
		this.rewardsCentral = rewardCentral;
		//this.executorService = Executors.newFixedThreadPool(1);
	}

	/*public RewardsService(GpsUtil gpsUtil, RewardCentral rewardCentral, int internalUserNumber) {
		this.gpsUtil = gpsUtil;
		this.rewardsCentral = rewardCentral;
		this.executorService = Executors.newFixedThreadPool(internalUserNumber);
	}*/
	
	public void setProximityBuffer(int proximityBuffer) {
		this.proximityBuffer = proximityBuffer;
	}
	
	public void setDefaultProximityBuffer() {
		proximityBuffer = defaultProximityBuffer;
	}

	/**
	 * Checks if each visited location of a user is near an attraction; if so, calculates reward points of the user
	 * @param user
	 */
	public void calculateRewards(User user) {
		List<Attraction> attractions = gpsUtil.getAttractions();
		
		for(VisitedLocation visitedLocation : user.getVisitedLocations()) {
			for(Attraction attraction : attractions) {
				if(user.getUserRewards().stream().noneMatch(reward -> reward.getAttraction().attractionName.equals(attraction.attractionName))) {
					calculateDistanceAndRewards(visitedLocation, attraction, user);
				}
			}
		}
	}

	/**
	 * Calculates distance between a location and an attraction, if the attraction is withing range of location, calculates rewards points
	 * @param visitedLocation by user
	 * @param attraction
	 * @param user
	 */
	public void calculateDistanceAndRewards(VisitedLocation visitedLocation, Attraction attraction, User user) {
		double distance = getDistance(attraction, visitedLocation.location);
		if(distance <= proximityBuffer) {
			UserReward userReward = new UserReward(visitedLocation, attraction, 0);
			calculatePointsAndSaveReward(user, attraction, userReward);
		}
	}

	/**
	 * Gets reward points for attraction and save them in user rewards
	 * @param user
	 * @param attraction
	 * @param userReward
	 */
	private void calculatePointsAndSaveReward(User user, Attraction attraction, UserReward userReward) {
		CompletableFuture.supplyAsync(() -> { return rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId()); }, executorService)
				.thenAccept(points -> {
					userReward.setRewardPoints(points);
					user.addUserReward(userReward);

				});
	}

	/**
	 * Checks attraction proximity to a location
	 * @param attraction
	 * @param location
	 * @return true if attraction is within range of the location
	 */
	public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
		return (getDistance(attraction, location) <= attractionProximityRange);
	}

	/**
	 * Gets reward points for an attraction and a user
	 * @param attraction
	 * @param userId
	 * @return number of reward points
	 */
	public int getRewardPoints(Attraction attraction, UUID userId) {
		return rewardsCentral.getAttractionRewardPoints(attraction.attractionId, userId);
	}

	/**
	 * Calculates distance between two locations
	 * @param loc1
	 * @param loc2
	 * @return distance
	 */
	public double getDistance(Location loc1, Location loc2) {
        double lat1 = Math.toRadians(loc1.latitude);
        double lon1 = Math.toRadians(loc1.longitude);
        double lat2 = Math.toRadians(loc2.latitude);
        double lon2 = Math.toRadians(loc2.longitude);

        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                               + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        double nauticalMiles = 60 * Math.toDegrees(angle);
        return STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
	}

}
