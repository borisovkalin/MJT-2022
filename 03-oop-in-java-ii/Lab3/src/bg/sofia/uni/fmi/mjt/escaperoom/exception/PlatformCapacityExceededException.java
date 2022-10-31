package bg.sofia.uni.fmi.mjt.escaperoom.exception;

public class PlatformCapacityExceededException extends Exception{
    PlatformCapacityExceededException(String msg,Throwable err){
        super(msg,err);
    }
}
