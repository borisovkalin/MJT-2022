package bg.sofia.uni.fmi.mjt.escaperoom.exception;

public class TeamNotFoundException extends Exception{
    public TeamNotFoundException(String msg,Throwable err){
        super(msg,err);
    }
}
