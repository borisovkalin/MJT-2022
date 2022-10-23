package bg.sofia.uni.fmi.mjt.airbnb.accommodation;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.location.Location;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public abstract class Accommodation implements Bookable{

    private final String id;
    private final Location location;
    private final double pricePerNight;

    private double totalPrice;
    private boolean booked;

    public Accommodation(String id, Location location, double pricePerNight){
        this.id = id;
        this.location = location;
        this.pricePerNight = pricePerNight;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public boolean isBooked() {
        return booked;
    }

    @Override
    public boolean book(LocalDateTime checkIn, LocalDateTime checkOut) {
        if(booked){
            return false;
        }

        if(checkIn == null || checkOut == null) {
            return false;
        }

        if(checkIn.isAfter(checkOut)){
            return false;
        }

        if(checkIn.isBefore(LocalDateTime.now())){
            return false;
        }

        long timeSpent = checkIn.until(checkOut, ChronoUnit.DAYS);

        totalPrice = pricePerNight;

        if(timeSpent != 0){
            totalPrice *= timeSpent;
        }

        booked = true;

        return true;
    }

    @Override
    public double getTotalPriceOfStay() {
        return totalPrice;
    }

    @Override
    public double getPricePerNight() {
        return pricePerNight;
    }
}
