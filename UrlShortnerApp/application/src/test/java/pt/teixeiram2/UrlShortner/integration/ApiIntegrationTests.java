package pt.teixeiram2.UrlShortner.integration;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pt.teixeiram2.UrlShortner.dto.CreateMappingRequest;
import pt.teixeiram2.UrlShortner.dto.CreateMappingResponse;
import pt.teixeiram2.UrlShortner.dto.FetchMappingResponse;
import pt.teixeiram2.UrlShortner.dto.InvalidRequestResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class ApiIntegrationTests extends AbstractIntegrationTests {


    @Test
    void contextLoads() {
        assertNotNull(getJdbcTemplate());
    }

    @Test
    void shouldCreateShortUrl() {
        String url = "http://www.example.com/path?key=value#";
        CreateMappingRequest createMappingRequest = new CreateMappingRequest();
        createMappingRequest.setFullUrl(url);

        CreateMappingResponse expectation = CreateMappingResponse.builder()
                .withUrl(url)
                .withShortUrl("3wslt8v")
                .build();

        ResponseEntity<CreateMappingResponse> createMappingResponseEntity =
                getRestTemplate().postForEntity(getCreateMappingUrl(), createMappingRequest, CreateMappingResponse.class);

        assertEquals(HttpStatus.CREATED.value(), createMappingResponseEntity.getStatusCode().value());
        assertEquals(expectation, createMappingResponseEntity.getBody());
    }

    @Test
    void shouldHandleConcurrentUrlCreation() throws InterruptedException {
        String url = "http://www.example.com/path?key=value#";
        long checksum = -6035612812965332595L;
        CreateMappingRequest createMappingRequest = new CreateMappingRequest();
        createMappingRequest.setFullUrl(url);

        ExecutorService executorService = Executors.newFixedThreadPool(50);
        List<Callable<ResponseEntity<CreateMappingResponse>>> requests = new ArrayList<>(50);
        for (int i = 0; i < 50; i++) {
            requests.add(() -> getRestTemplate().postForEntity(getCreateMappingUrl(), createMappingRequest, CreateMappingResponse.class));
        }
        executorService.invokeAll(requests);
        executorService.shutdown();
        boolean terminated = executorService.awaitTermination(10, TimeUnit.SECONDS);
        if (!terminated) {
            fail("Failed to terminate tasks before timeout");
        }

        int count = countDbRecordsWithChecksum(checksum);
        assertEquals(1, count);
    }

    @Test
    void shouldFetchFullUrl() {
        String expectedUrl = "http://www.example.com/path?key=value#";
        String shortUrl = "3wslt8v";
        Map<String, String> params = new HashMap<>();
        params.put("shortUrl", shortUrl);

        createMapping(expectedUrl);

        ResponseEntity<FetchMappingResponse> response =
                getRestTemplate().getForEntity(getFetchUrl(), FetchMappingResponse.class, params);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getFullUrl());
        assertEquals(expectedUrl, response.getBody().getFullUrl());
    }

    @Test
    void shouldReturnNotFoundIfNoMappingExists() {
        String shortUrl = "abcd";
        Map<String, String> params = new HashMap<>();
        params.put("shortUrl", shortUrl);
        ResponseEntity<String> response;
        try {
            response =
                    getRestTemplate().getForEntity(getFetchUrl(), String.class, params);
            assertEquals(404, response.getStatusCode().value());
            InvalidRequestResponse invalidRequestResponse = MAPPER.readValue(response.getBody(), InvalidRequestResponse.class);
            assertEquals(InvalidRequestResponse.NO_SUCH_MAPPING_ERROR, invalidRequestResponse.getError());
            assertEquals("Mapping not Found", invalidRequestResponse.getCause());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void createMapping(String url) {
        CreateMappingRequest createMappingRequest = new CreateMappingRequest();
        createMappingRequest.setFullUrl(url);
        ResponseEntity<CreateMappingResponse> createMappingResponseEntity =
                getRestTemplate().postForEntity(getCreateMappingUrl(), createMappingRequest, CreateMappingResponse.class);

        assertEquals(HttpStatus.CREATED.value(), createMappingResponseEntity.getStatusCode().value());
    }

    private int countDbRecordsWithChecksum(long checksum) {
        return jdbcTemplate.query(
                        "SELECT count(*) as cnt from url_mappings WHERE checksum = ?",
                        (rs, rowNum) -> rs.getInt("cnt"),
                        checksum)
                .stream()
                .findFirst().orElseThrow(() -> new RuntimeException("Failed to retrieve entity count"));
    }

}
