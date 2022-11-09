package bg.sofia.uni.fmi.mjt.flightscanner;

import bg.sofia.uni.fmi.mjt.flightscanner.airport.Airport;
import bg.sofia.uni.fmi.mjt.flightscanner.comparators.FlightByDestination;
import bg.sofia.uni.fmi.mjt.flightscanner.comparators.FlightByFreeSeats;
import bg.sofia.uni.fmi.mjt.flightscanner.flight.Flight;

import java.util.*;

public class FlightScanner implements FlightScannerAPI {

    private Map<Airport, Set<Flight>> flightsMap;
    private Map<Airport, Map<Airport, Flight>> adjacencyFlightList;



    public FlightScanner() {
        flightsMap = new HashMap<>();
        adjacencyFlightList = new HashMap<>();
    }
    @Override
    public void add(Flight flight) {
        validateFlight(flight);
        flightsMap.putIfAbsent(flight.getFrom(), new HashSet<>());
        flightsMap.get(flight.getFrom()).add(flight);

        addToAJ(flight);
    }

    @Override
    public void addAll(Collection<Flight> flights) {
        if (flights == null) {
            throw new IllegalArgumentException("flights cant be null");
        }
        List<Flight> temp = new ArrayList<>(flights);

        for (Flight flight: temp) {
            validateFlight(flight);
            flightsMap.putIfAbsent(flight.getFrom(), new HashSet<>());
            flightsMap.get(flight.getFrom()).add(flight);

            addToAJ(flight);
        }
    }

    @Override
    public List<Flight> searchFlights(Airport from, Airport to) {
        validateSearchParams(from, to);

        Map<Airport, Airport> prev = bfs(from);

        return reconstructPath(from, to, prev);
    }

    @Override
    public List<Flight> getFlightsSortedByFreeSeats(Airport from) {
        if (flightsMap.isEmpty()) {
            return List.of();
        }

        List<Flight> result = new ArrayList<>(flightsMap.get(from));
        result.sort(new FlightByFreeSeats());

        return List.copyOf(result);
    }

    @Override
    public List<Flight> getFlightsSortedByDestination(Airport from) {
        if (flightsMap.isEmpty()) {
            return List.of();
        }

        List<Flight> result = new ArrayList<>(flightsMap.get(from));
        result.sort(new FlightByDestination());

        return List.copyOf(result);
    }

    private void validateSearchParams(Airport from, Airport to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("from and to can't be null");
        }

        if (from.ID().equals(to.ID())) {
            throw new IllegalArgumentException("from and to are the same");
        }

    }

    private void validateAirport(Airport from) {
        if (from == null) {
            throw new IllegalArgumentException("from can't be null");
        }
    }


    private void validateFlight(Flight f) {
        if (f == null) {
            throw new IllegalArgumentException("flight can't be null");
        }
    }

    private void addToAJ(Flight flight) {
        if (!adjacencyFlightList.containsKey(flight.getFrom())) {
            Map<Airport, Flight> flightMap = new HashMap<>();
            flightMap.put(flight.getTo(), flight);

            adjacencyFlightList.put(flight.getFrom(), flightMap);
            return;
        }

        adjacencyFlightList.get(flight.getFrom()).putIfAbsent(flight.getTo(), flight);
    }

    private Map<Airport, Airport> bfs(Airport from) {
        Queue<Airport> q = new LinkedList<>();
        q.add(from);

        Set<Airport> visited = new HashSet<>();
        visited.add(from);
        Map<Airport, Airport> tempMap = new LinkedHashMap<>();

        while (!q.isEmpty()) {
            Airport node = q.poll();
            if (flightsMap.get(node) != null) {
                List<Flight> neighbours = new ArrayList<>(flightsMap.get(node));

                for (Flight next : neighbours) {
                    if (!visited.contains(next.getTo())) {
                        q.add(next.getTo());
                        visited.add(next.getTo());
                        tempMap.put(next.getTo(), node);
                    }
                }
            }
        }

        return tempMap;
    }

    private List<Flight> reconstructPath(Airport from, Airport to, Map<Airport, Airport> prev) {
        List<Flight> result = new ArrayList<>();

        for (Airport temp = to; prev.containsKey(temp); temp = prev.get(temp)) {
            result.add(adjacencyFlightList.get(prev.get(temp)).get(temp));
        }

        result = reverseArrayList(result);
        if (result.size() > 0) {
            if (result.get(0).getFrom().ID().equals(from.ID())) {
                return result;
            }
        }

        return new ArrayList<>();
    }

    private List<Flight> reverseArrayList(List<Flight> res) {
        List<Flight> newRes = new ArrayList<>();
        for (int i = res.size() - 1; i >= 0; i--) {
            newRes.add(res.get(i));
        }
        return newRes;
    }

}
