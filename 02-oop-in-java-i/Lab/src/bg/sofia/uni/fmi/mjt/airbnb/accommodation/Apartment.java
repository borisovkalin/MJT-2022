package bg.sofia.uni.fmi.mjt.airbnb.accommodation;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.location.Location;

public class Apartment extends Accommodation {

    private static final String APARTMENT_PREFIX = "APA-";
    private static long counter = 0;


    public Apartment(Location location, double pricePerNight) {
        super(APARTMENT_PREFIX + counter, location, pricePerNight);
        counter++;
    }

}
