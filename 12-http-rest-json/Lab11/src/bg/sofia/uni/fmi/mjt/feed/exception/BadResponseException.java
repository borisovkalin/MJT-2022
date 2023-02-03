package bg.sofia.uni.fmi.mjt.feed.exception;

import bg.sofia.uni.fmi.mjt.feed.util.URICreator;

public class BadResponseException extends RuntimeException {
    public BadResponseException(int statusCode, String msg) {
        super(statusCode + URICreator.DELIMITER + msg);
    }
}
