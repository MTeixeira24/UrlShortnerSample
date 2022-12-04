package pt.teixeiram2.UrlShortner.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pt.teixeiram2.UrlShortner.model.UrlMap;

import java.nio.ByteBuffer;

@Service
public class ShortUrlGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShortUrlGenerator.class);
    private static final short NUMERIC_START = 48;
    private static final int NUMERIC_SIZE = 9;
    private static final short ALPHA_START = 97;
    private static final int ALPHA_SIZE = 23;
    private static final int SIZE = ALPHA_SIZE + NUMERIC_SIZE;


    public UrlMap shortenUrl(String fullUrl, long checksum) {
        if(fullUrl == null || fullUrl.isBlank()) {
            LOGGER.error("operation='shortenUrl', msg='Attempted to generate short url for empty string'");
            throw new IllegalArgumentException("Attempted to generate short url for empty string");
        }

        LOGGER.debug("operation=shortenUrl, fullUrl={}, msg='Generating short URL'", fullUrl);
        ByteBuffer hashBytes = ByteBuffer.allocate(Long.BYTES).putLong(checksum).position(0);
        StringBuilder stringBuilder = new StringBuilder();
        while (hashBytes.position() < Long.BYTES) {
            char shortChar;
            short val = hashBytes.getShort();
            short mod = (short) Math.abs(val % SIZE);
            if (mod <= NUMERIC_SIZE) {
                shortChar = (char) (NUMERIC_START + mod);
            } else {
                shortChar = (char) (ALPHA_START + (mod - NUMERIC_SIZE));
            }
            stringBuilder.append(shortChar);
            LOGGER.debug("operation=shortenUrl, short={}, char={}, msg='Computed character'", val, shortChar);
        }
        String shortUrl = stringBuilder.toString();
        LOGGER.info("operation=shortenUrl, fullUrl={}, shortUrl={}, hash={}, msg='Generated short url'",
                fullUrl, shortUrl, checksum);

        return UrlMap.builder()
                .withShortUrl(shortUrl)
                .withChecksum(checksum)
                .withUrl(fullUrl)
                .build();
    }

    public long computeChecksum(String value) {
        long hash = 0;
        int length = value.length() >> 1;
        for (int i = 0; i < length; i++) {
            hash = 31 * hash + value.charAt(i);
        }
        LOGGER.debug("operation=shortenUrl, hash={}, msg='Computed 64-bit hash'", hash);
        return hash;
    }
}
