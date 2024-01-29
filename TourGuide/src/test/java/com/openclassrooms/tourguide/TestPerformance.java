package com.openclassrooms.tourguide;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.*;
import java.util.concurrent.TimeUnit;
import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import rewardCentral.RewardCentral;
import com.openclassrooms.tourguide.helper.InternalTestHelper;
import com.openclassrooms.tourguide.service.RewardsService;
import com.openclassrooms.tourguide.service.TourGuideService;
import com.openclassrooms.tourguide.model.User;

public class TestPerformance {

	/*
	 * A note on performance improvements:
	 * - highVolumeTrackLocation test for 100,000 users should be executed within 15 minutes
	 * - highVolumeGetRewards test for 100,000 users should be executed within 20 minutes
	 */

	@Disabled
	@Test
	public void highVolumeTrackLocation() {
        // GIVEN
        Map<Integer, Integer> nbUserTimeLimit = new HashMap<>();
        nbUserTimeLimit.put(100, 10);
        nbUserTimeLimit.put(1000, 10);
        nbUserTimeLimit.put(5000, 45);
        nbUserTimeLimit.put(10000, 90);
        nbUserTimeLimit.put(50000, 450);
        nbUserTimeLimit.put(100000, 900); // 15 minutes

        nbUserTimeLimit.keySet().forEach(nbUser -> {
            // WHEN
            long runTime = runTrackLocation(nbUser);

            // THEN
            System.out.println("highVolumeTrackLocation: Time Elapsed for " + nbUser + ": " + runTime + " seconds.");
            assertTrue(nbUserTimeLimit.get(nbUser) >= runTime);
        });
    }

    private long runTrackLocation(int nbUser) {
		GpsUtil gpsUtil = new GpsUtil();
		InternalTestHelper.setInternalUserNumber(nbUser);
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);

		List<User> allUsers = tourGuideService.getAllUsers();

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		allUsers.forEach(tourGuideService::trackUserLocation);

		stopWatch.stop();
		tourGuideService.tracker.stopTracking();

		return TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime());
	}

	@Disabled
	@Test
	public void highVolumeGetRewards() {
        // GIVEN
        Map<Integer, Integer> nbUserTimeLimit = new HashMap<>();
        nbUserTimeLimit.put(100, 10);
        nbUserTimeLimit.put(1000, 12);
        nbUserTimeLimit.put(10000, 120);
        nbUserTimeLimit.put(100000, 1200); // 20 minutes

        nbUserTimeLimit.keySet().forEach(nbUser -> {
            // WHEN
            long runTime = runGetRewards(nbUser);

            // THEN
            System.out.println("highVolumeGetRewards: Time Elapsed for " + nbUser + ": " + runTime + " seconds.");
            assertTrue(nbUserTimeLimit.get(nbUser) >= runTime);
        });
    }

    private long runGetRewards(int nbUser) {
		GpsUtil gpsUtil = new GpsUtil();
		InternalTestHelper.setInternalUserNumber(nbUser);
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		Attraction attraction = gpsUtil.getAttractions().get(0);
		List<User> allUsers = tourGuideService.getAllUsers();

		allUsers.forEach(user -> {
			user.clearVisitedLocations();
			user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
		});

		allUsers.forEach(user -> {
			while (user.getUserRewards().isEmpty()) {
				try {
					TimeUnit.MILLISECONDS.sleep(200);
				} catch (InterruptedException ignored) { }
			}
			assertFalse(user.getUserRewards().isEmpty());
		});

		stopWatch.stop();
		tourGuideService.tracker.stopTracking();

		return TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime());
	}

}
