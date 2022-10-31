package bg.sofia.uni.fmi.mjt.escaperoom.exception;

public class RoomAlreadyExistsException extends Exception{
    RoomAlreadyExistsException(String msg,Throwable err){
        super(msg,err);
    }
}
