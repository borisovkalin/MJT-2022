package bg.sofia.uni.fmi.mjt.escaperoom.exception;

public class PlatformCapacityExceededException extends RuntimeException {
    public PlatformCapacityExceededException(String msg) {
        super(msg);
    }
}
