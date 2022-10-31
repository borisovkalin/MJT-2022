package bg.sofia.uni.fmi.mjt.escaperoom.exception;

public class RoomNotFoundException extends Exception{
    public RoomNotFoundException(String msg,Throwable err){
        super(msg,err);
    }
}
