package com.openclassrooms.tourguide.service;

import com.openclassrooms.tourguide.model.Provider;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class TripPricerService {

    public TripPricerService() {}

    /**
     * Gets prices from several providers for an attraction
     * @param attractionId attraction to get prices for
     * @param adults number of adults
     * @param children number of children
     * @param nightsStay number of nights of the stay
     * @param rewardsPoints reward's points
     * @return
     */
    public List<Provider> getPrice(String apiKey, UUID attractionId, int adults, int children, int nightsStay, int rewardsPoints) {
        List<Provider> providers = new ArrayList<>();
        Set<String> providersUsed = new HashSet<>();

        do {
            int multiple = ThreadLocalRandom.current().nextInt(100, 700);
            double childrenDiscount = (double)(children / 3);
            double price = (double)(multiple * adults) + (double)multiple * childrenDiscount * (double)nightsStay + 0.99 - (double)rewardsPoints;
            if (price < 0.0) {
                price = 0.0;
            }

            String provider = getProviderName(providersUsed);
            if(!provider.isEmpty()) {
                providersUsed.add(provider);
                providers.add(new Provider(attractionId, provider, price));
            }
        } while(providers.size() < 10);

        return providers;
    }

    /**
     * Gets a random provider from a list of existing providers
     * @param providersUsed providers to ignore
     * @return provider name
     */
    private String getProviderName(Set<String> providersUsed) {
        List<String> providers = new ArrayList<>(Arrays.asList(
                "Holiday Travels", "Enterprize Ventures Limited", "Sunny Days", "FlyAway Trips",
                "United Partners Vacations", "Dream Trips", "Live Free", "Dancing Waves Cruselines and Partners",
                "AdventureCo", "Cure-Your-Blues"));
        providers.removeAll(providersUsed);

        if(!providers.isEmpty()) {
            int multiple = ThreadLocalRandom.current().nextInt(0, providers.size());
            return providers.get(multiple);
        } else {
            return "";
        }
    }
}
