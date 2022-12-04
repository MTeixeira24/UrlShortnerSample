package pt.teixeiram2.UrlShortner.converter;

public interface BiDirectionalConverter<A, B> {
    A convertTo(B bType);
    B convertFrom(A aType);
}
