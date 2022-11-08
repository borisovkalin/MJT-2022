package bg.sofia.uni.fmi.mjt.flightscanner.passenger;

public record Passenger(String id, String name, Gender gender) {

    public Passenger {
        if (id == null){
            throw new IllegalArgumentException("Passenger id can't be null");
        }
        if (name == null){
            throw new IllegalArgumentException("Passenger name can't be null");
        }
        if (gender == null){
            throw new IllegalArgumentException("Passenger gender can't be null");
        }

        if(id.isEmpty() || id.isBlank()){
            throw new IllegalArgumentException("Passenger id can't be blank/empty");
        }
        if(name.isEmpty() || name.isBlank()){
            throw new IllegalArgumentException("Passenger name can't be blank/empty");
        }
    }
}
