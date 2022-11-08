package bg.sofia.uni.fmi.mjt.flightscanner.flight;

import bg.sofia.uni.fmi.mjt.flightscanner.airport.Airport;
import bg.sofia.uni.fmi.mjt.flightscanner.exception.FlightCapacityExceededException;
import bg.sofia.uni.fmi.mjt.flightscanner.passenger.Passenger;

import java.util.Collection;

public class RegularFlight implements Flight{

    @Override
    public Airport getFrom() {
        return null;
    }

    @Override
    public Airport getTo() {
        return null;
    }

    @Override
    public void addPassenger(Passenger passenger) throws FlightCapacityExceededException {

    }

    @Override
    public void addPassengers(Collection<Passenger> passengers) throws FlightCapacityExceededException {

    }

    @Override
    public Collection<Passenger> getAllPassengers() {
        return null;
    }

    @Override
    public int getFreeSeatsCount() {
        return 0;
    }
}
