package com.openclassrooms.tourguide;

import java.util.List;

import com.openclassrooms.tourguide.model.*;
import gpsUtil.location.VisitedLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.tourguide.service.TourGuideService;

@RestController
public class TourGuideController {

	@Autowired
	TourGuideService tourGuideService;
	
    @RequestMapping("/")
    public String index() {
        return "Greetings from TourGuide!";
    }
    
    @RequestMapping("/getLocation") 
    public VisitedLocation getLocation(@RequestParam String userName) {
    	return tourGuideService.getUserLocation(getUser(userName));
    }
    
    @RequestMapping("/getNearbyAttractions")
    public UserLocation getNearbyAttractions(@RequestParam String userName) {
    	User user = getUser(userName);
        VisitedLocation visitedLocation = tourGuideService.getUserLocation(user);
        List<NearByAttraction> nearByAttractions = tourGuideService.getNearByAttractions(user.getUserId(), visitedLocation);

        return new UserLocation(user.getUserId(), userName, visitedLocation.location, nearByAttractions);
    }
    
    @RequestMapping("/getRewards") 
    public List<UserReward> getRewards(@RequestParam String userName) {
    	return tourGuideService.getUserRewards(getUser(userName));
    }
       
    @RequestMapping("/getTripDeals")
    public List<Provider> getTripDeals(@RequestParam String userName) {
    	return tourGuideService.getTripDeals(getUser(userName));
    }
    
    private User getUser(String userName) {
    	return tourGuideService.getUser(userName);
    }
   

}