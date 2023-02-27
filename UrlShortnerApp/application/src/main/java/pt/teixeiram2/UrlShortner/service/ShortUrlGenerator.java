package pt.teixeiram2.UrlShortner.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pt.teixeiram2.UrlShortner.model.UrlMap;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShortUrlGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShortUrlGenerator.class);
    private static final int PART_SPLIT_COUNT = 6;
    private static final short NUMERIC_START = 48;
    private static final int NUMERIC_SIZE = 9;
    private static final short ALPHA_START = 97;
    private static final int ALPHA_SIZE = 23;
    private static final int SIZE = ALPHA_SIZE + NUMERIC_SIZE;


    public UrlMap shortenUrl(String fullUrl, long checksum) {
        if (fullUrl == null || fullUrl.isBlank()) {
            LOGGER.error("operation='shortenUrl', msg='Attempted to generate short url for empty string'");
            throw new IllegalArgumentException("Attempted to generate short url for empty string");
        }

        LOGGER.debug("operation=shortenUrl, fullUrl={}, msg='Generating short URL'", fullUrl);

        String shortUrl = splitString(fullUrl)
                .stream()
                .map(String::hashCode)
                .map(this::toAlphaNumeric)
                .collect(Collectors.joining());

        LOGGER.info("operation=shortenUrl, fullUrl={}, shortUrl={}, hash={}, msg='Generated short url'",
                fullUrl, shortUrl, checksum);

        return UrlMap.builder()
                .withShortUrl(shortUrl)
                .withChecksum(checksum)
                .withUrl(fullUrl)
                .build();
    }

    private String toAlphaNumeric(Integer hash) {
        String shortChar;
        short mod = (short) Math.abs(hash % SIZE);
        if (mod <= NUMERIC_SIZE) {
            shortChar = String.valueOf((char) (NUMERIC_START + mod));
        } else {
            shortChar = String.valueOf((char) (ALPHA_START + (mod - NUMERIC_SIZE)));
        }
        return shortChar;
    }

    private List<String> splitString(String urlString) {
        int segmentSize = urlString.length() / PART_SPLIT_COUNT;
        List<String> result = new ArrayList<>(8);
        for (int i = 0; i < urlString.length(); i += segmentSize) {
            int endIndex = i + segmentSize;
            if (endIndex > urlString.length()) {
                endIndex = urlString.length();
            }
            result.add(urlString.substring(i, endIndex));
        }
        return result;
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
