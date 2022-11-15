package bg.sofia.uni.fmi.mjt.flightscanner.flight;

import bg.sofia.uni.fmi.mjt.flightscanner.airport.Airport;
import bg.sofia.uni.fmi.mjt.flightscanner.exception.FlightCapacityExceededException;
import bg.sofia.uni.fmi.mjt.flightscanner.exception.InvalidFlightException;
import bg.sofia.uni.fmi.mjt.flightscanner.passenger.Passenger;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class RegularFlight implements Flight {
    private String flightId;
    private Airport from;
    private Airport to;
    private int totalCapacity;
    private int currentCapacity;

    private Set<Passenger> passengerSet;

    private RegularFlight(String flightId, Airport from, Airport to, int totalCapacity) {
        this.flightId = flightId;
        this.from = from;
        this.to = to;
        this.totalCapacity = totalCapacity;
        this.passengerSet = new HashSet<>();
    }

    public static RegularFlight of(String flightId, Airport from, Airport to, int totalCapacity) {
        if (flightId == null) {
            throw new IllegalArgumentException("Flight Id can't be null");
        }

        if (flightId.isBlank() || flightId.isEmpty()) {
            throw new IllegalArgumentException("Flight Id can't be blank/empty");
        }
        if (from == null || to == null) {
            throw new IllegalArgumentException("from and to can't be null");
        }
        if (totalCapacity < 0) {
            throw new IllegalArgumentException("totalCapacity can't be negative");
        }

        if (from.ID().equals(to.ID())) {
            throw new InvalidFlightException("Airports are the same");
        }

        return new RegularFlight(flightId, from, to, totalCapacity);
    }


    @Override
    public Airport getFrom() {
        return from;
    }

    @Override
    public Airport getTo() {
        return to;
    }

    @Override
    public void addPassenger(Passenger passenger) throws FlightCapacityExceededException {
        if (currentCapacity >= totalCapacity) {
            throw new FlightCapacityExceededException("Couldn't add passenger");
        }
        passengerSet.add(passenger);
        currentCapacity++;
    }

    @Override
    public void addPassengers(Collection<Passenger> passengers) throws FlightCapacityExceededException {
        int newPassengerCount = passengers.size();
        if ((currentCapacity + newPassengerCount) > totalCapacity) {
            throw new FlightCapacityExceededException("Couldn't add all passengers to flight");
        }
        passengerSet.addAll(passengers);
        currentCapacity += newPassengerCount;
    }

    @Override
    public Collection<Passenger> getAllPassengers() {
        return Collections.unmodifiableSet(passengerSet);
    }

    @Override
    public int getFreeSeatsCount() {
        return totalCapacity - currentCapacity;
    }

    public String getFlightId() {
        return flightId;
    }

}
