package bg.sofia.uni.fmi.mjt.airbnb;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.Accommodation;
import bg.sofia.uni.fmi.mjt.airbnb.accommodation.Bookable;
import bg.sofia.uni.fmi.mjt.airbnb.filter.Criterion;

import java.awt.print.Book;

public class Airbnb implements AirbnbAPI{

    private final Bookable[] accommodations;
    public Airbnb(Bookable[] accommodations){
        this.accommodations = accommodations;
    }
    @Override
    public Bookable findAccommodationById(String id) {
        if(id == null || id.isBlank()) {
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
        double result = 0d;

        for (Bookable accommodation : accommodations) {
            result += accommodation.getTotalPriceOfStay();
        }

        return result;
    }

    @Override
    public long countBookings() {
        long counter = 0L;

        for(Bookable accommodation: accommodations){
            if(accommodation.isBooked()){
                counter++;
            }
        }
        return counter;
    }

    @Override
    public Bookable[] filterAccommodations(Criterion... criteria) {//todo
    


        return new Bookable[0];
    }
}
