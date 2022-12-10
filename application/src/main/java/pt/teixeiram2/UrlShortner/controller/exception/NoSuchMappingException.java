package pt.teixeiram2.UrlShortner.controller.exception;

public class NoSuchMappingException extends RuntimeException {
    public NoSuchMappingException(String s) {
        super(s);
    }
}
