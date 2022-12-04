package pt.teixeiram2.UrlShortner.dto;

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
