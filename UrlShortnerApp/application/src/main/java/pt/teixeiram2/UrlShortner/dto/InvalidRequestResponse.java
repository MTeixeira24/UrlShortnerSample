package pt.teixeiram2.UrlShortner.dto;

import java.util.Objects;
import java.util.StringJoiner;

public final class InvalidRequestResponse {

    public static final String INVALID_REQUEST_ERROR = "Invalid Request";
    public static final String NO_SUCH_MAPPING_ERROR = "Mapping not found";

    private String error;
    private String cause;

    public InvalidRequestResponse() {

    }

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

    public void setError(String error) {
        this.error = error;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvalidRequestResponse that = (InvalidRequestResponse) o;
        return Objects.equals(error, that.error) && Objects.equals(cause, that.cause);
    }

    @Override
    public int hashCode() {
        return Objects.hash(error, cause);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", InvalidRequestResponse.class.getSimpleName() + "[", "]")
                .add("error='" + error + "'")
                .add("cause='" + cause + "'")
                .toString();
    }
}
