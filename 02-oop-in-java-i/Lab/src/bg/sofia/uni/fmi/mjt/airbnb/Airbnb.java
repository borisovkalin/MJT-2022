package bg.sofia.uni.fmi.mjt.airbnb;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.Bookable;
import bg.sofia.uni.fmi.mjt.airbnb.filter.Criterion;

public class Airbnb implements AirbnbAPI{

    public Airbnb(Bookable[] accommodations){}
    @Override
    public Bookable findAccommodationById(String id) { //todo
        return null;
    }

    @Override
    public double estimateTotalRevenue() { // todo
        return 0;
    }

    @Override
    public long countBookings() { //todo
        return 0;
    }

    @Override
    public Bookable[] filterAccommodations(Criterion... criteria) { //todo
        return new Bookable[0];
    }
}
