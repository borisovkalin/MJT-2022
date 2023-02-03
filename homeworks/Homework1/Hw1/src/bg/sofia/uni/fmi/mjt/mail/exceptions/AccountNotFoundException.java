package bg.sofia.uni.fmi.mjt.mail.exceptions;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String msg) {
        super(msg);
    }
}
