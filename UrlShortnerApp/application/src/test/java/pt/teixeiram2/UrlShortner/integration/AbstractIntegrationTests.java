package pt.teixeiram2.UrlShortner.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.net.URI;
import java.net.URISyntaxException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
@Testcontainers
public class AbstractIntegrationTests {
    protected static final ObjectMapper MAPPER = new ObjectMapper();

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

    protected String getFetchUrl() {
        return fetchUrl;
    }

    public TestRestTemplate getRestTemplate() {
        return restTemplate;
    }

    public URI getCreateMappingUrl() {
        return createMappingUrl;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    private void cleanDatastore() {
        jdbcTemplate.execute("TRUNCATE TABLE url_mappings");
    }

}
