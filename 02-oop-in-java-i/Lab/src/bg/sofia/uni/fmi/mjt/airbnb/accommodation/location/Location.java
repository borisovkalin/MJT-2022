package bg.sofia.uni.fmi.mjt.airbnb.accommodation.location;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Location {

    private final double x;
    private final double y;

    public Location(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double distanceTo(Location other) {
        double diffX = pow(x - other.x, 2);
        double diffY = pow(y - other.y, 2);

        return sqrt(diffX + diffY);
    }
}
