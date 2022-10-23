package bg.sofia.uni.fmi.mjt.airbnb.accommodation;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.location.Location;

public class Villa extends Accommodation {
    private static final String VILLA_PREFIX = "VIL-";
    private static long counter = 0;


    public Villa(Location location, double pricePerNight){
        super(VILLA_PREFIX + counter, location, pricePerNight);
        counter++;
    }
}
