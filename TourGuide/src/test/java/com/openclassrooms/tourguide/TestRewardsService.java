package com.openclassrooms.tourguide;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.junit.jupiter.api.Test;

import rewardCentral.RewardCentral;
import com.openclassrooms.tourguide.helper.InternalTestHelper;
import com.openclassrooms.tourguide.service.RewardsService;
import com.openclassrooms.tourguide.service.TourGuideService;
import com.openclassrooms.tourguide.model.User;
import com.openclassrooms.tourguide.model.UserReward;

public class TestRewardsService {

	@Test
	public void userGetRewards() {
		// GIVEN
		GpsUtil gpsUtil = new GpsUtil();
		InternalTestHelper.setInternalUserNumber(0);
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		assertEquals(0, user.getUserRewards().size());

		// WHEN
		Attraction attraction = gpsUtil.getAttractions().get(0);
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
		rewardsService.calculateRewards(user);
		sleep(1);

		tourGuideService.tracker.stopTracking();

		// THEN
        assertEquals(1, user.getUserRewards().size());
	}

	@Test
	public void isWithinAttractionProximity() {
		// GIVEN
		GpsUtil gpsUtil = new GpsUtil();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());

		// WHEN
		Attraction attraction = gpsUtil.getAttractions().get(0);

		// THEN
		assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
	}

	@Test
	public void nearAllAttractions() {
		// GIVEN
		GpsUtil gpsUtil = new GpsUtil();
		InternalTestHelper.setInternalUserNumber(1);
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);
		rewardsService.setProximityBuffer(Integer.MAX_VALUE);

		// WHEN
		rewardsService.calculateRewards(tourGuideService.getAllUsers().get(0));
		sleep(15);

		tourGuideService.tracker.stopTracking();

		// THEN
		List<UserReward> userRewards = tourGuideService.getUserRewards(tourGuideService.getAllUsers().get(0));
		assertEquals(gpsUtil.getAttractions().size(), userRewards.size());
	}

	private void sleep(int seconds) {
		try {
			TimeUnit.SECONDS.sleep(seconds);
		} catch (InterruptedException ignored) { }
	}

}
