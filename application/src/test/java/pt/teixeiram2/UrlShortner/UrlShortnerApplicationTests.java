package pt.teixeiram2.UrlShortner;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;
import pt.teixeiram2.UrlShortner.controller.UrlMappingController;
import pt.teixeiram2.UrlShortner.model.UrlMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Testcontainers
class UrlShortnerApplicationTests {

    @Autowired
    private UrlMappingController urlMappingController;

    @Test
    void contextLoads() {
    }

    @Test
    void shouldCreateMappings() {
        String url = "www.example.com/path?myQuery=value#";
        String shortUrl = "cmmq";
        long checkSum = -7749012243208215097L;

        UrlMap urlMap = urlMappingController.createMapping(url);

        assertNotNull(urlMap);
        assertEquals(checkSum, urlMap.getChecksum());
        assertEquals(url, urlMap.getUrl());
        assertEquals(shortUrl, urlMap.getShortUrl());
    }

}
