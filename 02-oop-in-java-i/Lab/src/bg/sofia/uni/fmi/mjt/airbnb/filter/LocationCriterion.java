package bg.sofia.uni.fmi.mjt.airbnb.filter;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.Bookable;
import bg.sofia.uni.fmi.mjt.airbnb.accommodation.location.Location;

import static java.lang.Math.abs;

public class LocationCriterion implements Criterion{
    private final Location currentLocation;
    private final double maxDistance;

    public LocationCriterion(Location currentLocation, double maxDistance){
        this.currentLocation = currentLocation;
        this.maxDistance = maxDistance;
    }
    @Override
    public boolean check(Bookable bookable) {
        if(bookable == null) {
            return false;
        }
        return maxDistance >= (abs(currentLocation.getX() - bookable.getLocation().getX()) + abs(currentLocation.getY() - bookable.getLocation().getY()));
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public double getMaxDistance() {
        return maxDistance;
    }
}
