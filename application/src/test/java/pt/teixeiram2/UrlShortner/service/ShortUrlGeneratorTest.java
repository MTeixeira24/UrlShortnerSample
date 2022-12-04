package pt.teixeiram2.UrlShortner.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import pt.teixeiram2.UrlShortner.model.UrlMap;

import static org.junit.jupiter.api.Assertions.*;

class ShortUrlGeneratorTest {

    private ShortUrlGenerator victim;

    @BeforeEach
    void setUp() {
        victim = new ShortUrlGenerator();
    }

    @Test
    void shouldShortenUrl() {
        String url = "www.example.com/path?myQuery=value#";
        UrlMap result = victim.shortenUrl(url, -7749012243208215097L);
        assertEquals("cmmq", result.getShortUrl());
        assertEquals(url, result.getUrl());
        assertEquals(-7749012243208215097L, result.getChecksum());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowOnIllegalArgument(String arg) {
        assertThrows(IllegalArgumentException.class, () -> victim.shortenUrl(arg, 0L));
    }

}