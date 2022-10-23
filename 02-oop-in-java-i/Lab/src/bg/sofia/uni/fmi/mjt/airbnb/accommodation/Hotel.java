package bg.sofia.uni.fmi.mjt.airbnb.accommodation;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.location.Location;

public class Hotel extends Accommodation {
    private static final String HOTEL_PREFIX = "HOT-";
    private static long counter = 0;


    public Hotel(Location location, double pricePerNight){
        super(HOTEL_PREFIX + counter, location, pricePerNight);
        counter++;
    }
}
