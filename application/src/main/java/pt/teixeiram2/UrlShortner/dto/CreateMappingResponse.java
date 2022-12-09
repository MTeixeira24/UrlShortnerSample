package pt.teixeiram2.UrlShortner.dto;

import java.util.Objects;
import java.util.StringJoiner;

public final class CreateMappingResponse {

    private final String url;
    private final String shortUrl;


    public static CreateMappingResponseBuilder builder() {
        return new CreateMappingResponseBuilder();
    }

    public CreateMappingResponse(String url, String shortUrl) {
        this.url = url;
        this.shortUrl = shortUrl;
    }

    public String getUrl() {
        return url;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateMappingResponse that = (CreateMappingResponse) o;
        return Objects.equals(url, that.url) && Objects.equals(shortUrl, that.shortUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, shortUrl);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CreateMappingResponse.class.getSimpleName() + "[", "]")
                .add("url='" + url + "'")
                .add("shortUrl='" + shortUrl + "'")
                .toString();
    }

    public static final class CreateMappingResponseBuilder {
        private String url;
        private String shortUrl;

        private CreateMappingResponseBuilder() {
        }

        public CreateMappingResponseBuilder withUrl(String url) {
            this.url = url;
            return this;
        }

        public CreateMappingResponseBuilder withShortUrl(String shortUrl) {
            this.shortUrl = shortUrl;
            return this;
        }

        public CreateMappingResponse build() {
            return new CreateMappingResponse(url, shortUrl);
        }
    }
}
