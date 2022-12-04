package pt.teixeiram2.UrlShortner.dto;

import java.util.Objects;
import java.util.StringJoiner;

public class CreateMappingRequest {

    private String fullUrl;

    public String getFullUrl() {
        return fullUrl;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateMappingRequest that = (CreateMappingRequest) o;
        return Objects.equals(fullUrl, that.fullUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullUrl);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CreateMappingRequest.class.getSimpleName() + "[", "]")
                .add("fullUrl='" + fullUrl + "'")
                .toString();
    }
}
