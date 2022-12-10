package pt.teixeiram2.UrlShortner.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;
import pt.teixeiram2.UrlShortner.dto.CreateMappingRequest;
import pt.teixeiram2.UrlShortner.dto.CreateMappingResponse;
import pt.teixeiram2.UrlShortner.dto.FetchMappingResponse;
import pt.teixeiram2.UrlShortner.dto.InvalidRequestResponse;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class UrlShortnerIntegrationTests {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    JdbcTemplate jdbcTemplate;
    @LocalServerPort
    private int port;
    private String baseUrl;
    private URI createMappingUrl;
    private String fetchUrl;
    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void setUp() throws URISyntaxException {
        baseUrl = "http://localhost:" + port + "/";
        createMappingUrl = new URI(baseUrl + "shorten/createMapping");
        fetchUrl = baseUrl + "shorten/getFullUrl?shortUrl={shortUrl}";
        cleanDatastore();
    }

    private void cleanDatastore() {
        jdbcTemplate.execute("TRUNCATE TABLE url_mappings");
    }

    @Test
    void contextLoads() {
        assertNotNull(jdbcTemplate);
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
                restTemplate.postForEntity(createMappingUrl, createMappingRequest, CreateMappingResponse.class);

        assertEquals(200, createMappingResponseEntity.getStatusCode().value());
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
            requests.add(() -> restTemplate.postForEntity(createMappingUrl, createMappingRequest, CreateMappingResponse.class));
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
                restTemplate.getForEntity(fetchUrl, FetchMappingResponse.class, params);

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
                    restTemplate.getForEntity(fetchUrl, String.class, params);
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
                restTemplate.postForEntity(createMappingUrl, createMappingRequest, CreateMappingResponse.class);

        assertEquals(200, createMappingResponseEntity.getStatusCode().value());
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
