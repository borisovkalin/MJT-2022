package bg.sofia.uni.fmi.mjt.mail.exceptions;

public class AccountAlreadyExistsException extends RuntimeException {

    public AccountAlreadyExistsException(String msg) {
        super(msg);
    }
}
