package bg.sofia.uni.fmi.mjt.feed.error;

public enum ErrorResponse {
    OK(200, "OK. The request was executed successfully"),
    BAD_REQUEST(400, "The request was unacceptable, often due to a missing or misconfigured parameter."),
    UNAUTHORIZED(401, "Your API key was missing from the request, or wasn't correct."),
    TOO_MANY_REQUESTS(4, "Too Many Requests. You made too many requests within a " +
            "window of time and have been rate limited. Back off for a while."),
    SERVER_ERROR(500, "Server Error. Something went wrong on our side.");

    public final int code;
    public final String message;

    private ErrorResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
