package bg.sofia.uni.fmi.mjt.airbnb;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.Bookable;
import bg.sofia.uni.fmi.mjt.airbnb.filter.Criterion;

public class Airbnb implements AirbnbAPI{

    private Bookable[] accommodations;
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
    public Bookable[] filterAccommodations(Criterion... criteria) {//tod
        long counter = 0;
        for (Bookable accommodation : accommodations) {
            boolean flag = true;
            for(Criterion criterion : criteria){
                if(!criterion.check(accommodation)){
                    flag = false;
                }
            }

            if(flag){
                counter++;
            }
        }
        Bookable[] result = new Bookable[(int)counter];

        counter = 0L;
        for (Bookable accommodation : accommodations) {
            boolean flag = true;
            for(Criterion criterion : criteria){
                if(!criterion.check(accommodation)){
                    flag = false;
                }
            }

            if(flag){
                result[(int)counter] = accommodation;
                counter++;
            }
        }

        return result;
    }
}
