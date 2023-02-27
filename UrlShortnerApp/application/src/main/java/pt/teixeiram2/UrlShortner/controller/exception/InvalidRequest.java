package pt.teixeiram2.UrlShortner.controller.exception;

public class InvalidRequest extends RuntimeException {
    public InvalidRequest(String s) {
        super(s);
    }
}
