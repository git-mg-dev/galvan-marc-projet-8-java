package com.openclassrooms.tourguide;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.openclassrooms.tourguide.model.NearByAttraction;
import com.openclassrooms.tourguide.model.Provider;
import com.openclassrooms.tourguide.model.User;
import gpsUtil.GpsUtil;
import gpsUtil.location.VisitedLocation;
import org.junit.jupiter.api.Test;

import rewardCentral.RewardCentral;
import com.openclassrooms.tourguide.helper.InternalTestHelper;
import com.openclassrooms.tourguide.service.RewardsService;
import com.openclassrooms.tourguide.service.TourGuideService;

public class TestTourGuideService {

	@Test
	public void getUserLocation() {
		// GIVEN
		GpsUtil gpsUtil = new GpsUtil();
		InternalTestHelper.setInternalUserNumber(0);
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		tourGuideService.addUser(user);

		// WHEN
		tourGuideService.trackUserLocation(user);
		sleep();
		tourGuideService.tracker.stopTracking();

		// THEN
		VisitedLocation visitedLocation = tourGuideService.getUserLocation(user);
		assertEquals(visitedLocation.userId, user.getUserId());
	}

	@Test
	public void addUser() {
		// GIVEN
		GpsUtil gpsUtil = new GpsUtil();
		InternalTestHelper.setInternalUserNumber(0);
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

		// WHEN
		tourGuideService.addUser(user);
		tourGuideService.addUser(user2);

		User retrievedUser = tourGuideService.getUser(user.getUserName());
		User retrievedUser2 = tourGuideService.getUser(user2.getUserName());
		tourGuideService.tracker.stopTracking();

		// THEN
		assertEquals(user, retrievedUser);
		assertEquals(user2, retrievedUser2);
	}

	@Test
	public void getAllUsers() {
		// GIVEN
		GpsUtil gpsUtil = new GpsUtil();
		InternalTestHelper.setInternalUserNumber(0);
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

		tourGuideService.addUser(user);
		tourGuideService.addUser(user2);

		// WHEN
		List<User> allUsers = tourGuideService.getAllUsers();

		tourGuideService.tracker.stopTracking();

		// THEN
		assertTrue(allUsers.contains(user));
		assertTrue(allUsers.contains(user2));
	}

	@Test
	public void trackUser() {
		// GIVEN
		GpsUtil gpsUtil = new GpsUtil();
		InternalTestHelper.setInternalUserNumber(0);
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		tourGuideService.addUser(user);

		// WHEN
		tourGuideService.trackUserLocation(user);
		sleep();

		tourGuideService.tracker.stopTracking();

		// THEN
		VisitedLocation visitedLocation = tourGuideService.getUserLocation(user);
		assertEquals(user.getUserId(), visitedLocation.userId);
	}

	@Test
	public void getNearbyAttractions() {
		// GIVEN
		GpsUtil gpsUtil = new GpsUtil();
		InternalTestHelper.setInternalUserNumber(0);
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		tourGuideService.addUser(user);

		// WHEN
		tourGuideService.trackUserLocation(user);
		sleep();

		VisitedLocation visitedLocation = tourGuideService.getUserLocation(user);
		List<NearByAttraction> nearByAttractions = tourGuideService.getNearByAttractions(user.getUserId(), visitedLocation);

		tourGuideService.tracker.stopTracking();

		// THEN
		assertEquals(5, nearByAttractions.size());
	}

	@Test
	public void getTripDeals() {
		// GIVEN
		GpsUtil gpsUtil = new GpsUtil();
		InternalTestHelper.setInternalUserNumber(0);
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		tourGuideService.addUser(user);

		// WHEN
		List<Provider> providers = tourGuideService.getTripDeals(user);
		tourGuideService.tracker.stopTracking();

		// THEN
		assertEquals(10, providers.size());
	}

	private void sleep() {
		try {
			TimeUnit.MILLISECONDS.sleep(100);
		} catch (InterruptedException ignored) { }
	}
}
