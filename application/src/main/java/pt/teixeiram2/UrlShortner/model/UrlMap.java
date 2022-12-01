package pt.teixeiram2.UrlShortner.model;

import java.util.Objects;
import java.util.StringJoiner;

public final class UrlMap {

    private final String url;
    private final String shortUrl;
    private final long checksum;

    private UrlMap(UrlMapBuilder builder) {
        this.url = builder.url;
        this.shortUrl = builder.shortUrl;
        this.checksum = builder.checksum;
    }

    public static UrlMapBuilder builder() {
        return new UrlMapBuilder();
    }

    public String getUrl() {
        return url;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public long getChecksum() {
        return checksum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UrlMap urlMap = (UrlMap) o;
        return checksum == urlMap.checksum && Objects.equals(url, urlMap.url) && Objects.equals(shortUrl, urlMap.shortUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, shortUrl, checksum);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", UrlMap.class.getSimpleName() + "[", "]")
                .add("url='" + url + "'")
                .add("shortUrl='" + shortUrl + "'")
                .add("checksum=" + checksum)
                .toString();
    }

    public static final class UrlMapBuilder {
        private String url;
        private String shortUrl;
        private long checksum;

        private UrlMapBuilder() {
        }

        public static UrlMapBuilder anUrlMap() {
            return new UrlMapBuilder();
        }

        public UrlMapBuilder withUrl(String url) {
            this.url = url;
            return this;
        }

        public UrlMapBuilder withShortUrl(String shortUrl) {
            this.shortUrl = shortUrl;
            return this;
        }

        public UrlMapBuilder withChecksum(long checksum) {
            this.checksum = checksum;
            return this;
        }

        public UrlMap build() {
            return new UrlMap(this);
        }
    }
}
