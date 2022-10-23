package bg.sofia.uni.fmi.mjt.airbnb.filter;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.Bookable;

public class PriceCriterion implements Criterion{

    private final double minPrice;
    private final double maxPrice;

    public PriceCriterion(double minPrice, double maxPrice){
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
    }

    @Override
    public boolean check(Bookable bookable) { //todo
        return false;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }
}
