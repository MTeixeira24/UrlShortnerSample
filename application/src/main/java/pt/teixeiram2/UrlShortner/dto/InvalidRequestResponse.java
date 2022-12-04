package pt.teixeiram2.UrlShortner.dto;

public final class InvalidRequestResponse {

    public static final String INVALID_REQUEST_ERROR = "Invalid Request";

    private final String error;
    private final String cause;

    public InvalidRequestResponse(String error, String cause) {
        this.error = error;
        this.cause = cause;
    }

    public String getError() {
        return error;
    }

    public String getCause() {
        return cause;
    }
}
