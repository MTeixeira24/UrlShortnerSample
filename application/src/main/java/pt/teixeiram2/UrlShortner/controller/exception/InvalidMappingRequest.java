package pt.teixeiram2.UrlShortner.controller.exception;

public class InvalidMappingRequest extends RuntimeException {
    public InvalidMappingRequest(String s) {
        super(s);
    }
}
