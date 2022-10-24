package bg.sofia.uni.fmi.mjt.airbnb;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.Bookable;
import bg.sofia.uni.fmi.mjt.airbnb.filter.Criterion;

public class Airbnb implements AirbnbAPI {

    private final Bookable[] accommodations;

    public Airbnb(Bookable[] accommodations) {
        this.accommodations = accommodations;
    }

    @Override
    public Bookable findAccommodationById(String id) {
        if (id == null || id.isBlank()) {
            return null;
        }

        for (Bookable accommodation : accommodations) {
            if (accommodation.getId().equalsIgnoreCase(id)) {
                return accommodation;
            }
        }

        return null;
    }

    @Override
    public double estimateTotalRevenue() {
        double result = 0.0;

        for (Bookable accommodation : accommodations) {
            result += accommodation.getTotalPriceOfStay();
        }

        return result;
    }

    @Override
    public long countBookings() {
        long bookings = 0L;

        for (Bookable accommodation : accommodations) {
            if (accommodation.isBooked()) {
                bookings++;
            }
        }

        return bookings;
    }

    public boolean matchCriteria(Bookable accommodation, Criterion... criteria) {
        for (Criterion criterion : criteria) {
            if (!criterion.check(accommodation)) {
                return false;
            }
        }

        return true;
    }

    public int countFilteredAccommodations(Bookable[] accommodations, Criterion... criteria) {
        int counter = 0;

        for (Bookable accommodation : accommodations) {
            if (matchCriteria(accommodation, criteria)) {
                counter++;
            }
        }

        return counter;
    }

    @Override
    public Bookable[] filterAccommodations(Criterion... criteria) {

        int counter = countFilteredAccommodations(accommodations, criteria);
        Bookable[] result = new Bookable[counter];

        counter = 0;

        for (Bookable accommodation : accommodations) {
            if (matchCriteria(accommodation, criteria)) {
                result[counter] = accommodation;
                counter++;
            }

        }

        return result;
    }
}
