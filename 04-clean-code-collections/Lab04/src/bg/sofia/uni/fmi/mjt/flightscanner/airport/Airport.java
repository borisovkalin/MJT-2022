package bg.sofia.uni.fmi.mjt.flightscanner.airport;

public record Airport(String ID) {

    public Airport {
        if (ID == null) {
            throw new IllegalArgumentException("Airport ID can't be null");
        }

        if (ID.isBlank() || ID.isEmpty()) {
            throw new IllegalArgumentException("Airport ID can't be an empty/blank string");
        }
    }
}
