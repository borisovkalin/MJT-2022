import bg.sofia.uni.fmi.mjt.flightscanner.FlightScanner;
import bg.sofia.uni.fmi.mjt.flightscanner.airport.Airport;
import bg.sofia.uni.fmi.mjt.flightscanner.flight.Flight;
import bg.sofia.uni.fmi.mjt.flightscanner.flight.RegularFlight;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Airport a1 = new Airport("Air1");
        Airport a2 = new Airport("Air2");
        Airport a3 = new Airport("Air3");
        Airport a4 = new Airport("Air4");
        Airport a5 = new Airport("Air5");
        Airport a6 = new Airport("Air6");

        FlightScanner fs = new FlightScanner();
        //(String flightId, Airport from, Airport to, int totalCapacity
        RegularFlight between1 = RegularFlight.of("#123", a1, a2, 16);
        RegularFlight between2 = RegularFlight.of("#456", a1, a5, 12);
        RegularFlight between3 = RegularFlight.of("#345", a1, a2, 13);
        RegularFlight between4 = RegularFlight.of("#13", a2, a3, 11);
        RegularFlight between5 = RegularFlight.of("#836", a3, a1, 12);
        RegularFlight between6 = RegularFlight.of("#534", a3, a4, 3);
        RegularFlight between7 = RegularFlight.of("#999", a4, a6, 14);
        System.out.println(between1.getAllPassengers());
        List<Flight> arr = new ArrayList<>();
        arr.add(between1);
        arr.add(between2);
        arr.add(between3);
        arr.add(between4);
        arr.add(between5);
        arr.add(between6);

        fs.add(between7);
        fs.addAll(arr);

        List<Flight> printList = fs.getFlightsSortedByFreeSeats(a1);

        for (Flight a: printList) {
            System.out.println(a.getFrom().ID() + " " + a.getTo().ID() + " ");
        }

        List<Flight> printList2 = fs.searchFlights(a6, a4);

        for (Flight a: printList2) {
            System.out.println(a.getFrom().ID() + " " + a.getTo().ID() + " ");
        }
    }

}
