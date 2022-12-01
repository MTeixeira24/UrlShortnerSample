package pt.teixeiram2.UrlShortner;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;
import pt.teixeiram2.UrlShortner.service.UrlMappingService;
import pt.teixeiram2.UrlShortner.model.UrlMappingEntity;

import java.math.BigInteger;
import java.sql.Date;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class UrlShortnerApplicationTests {

	@Autowired
	private UrlMappingService urlMappingService;

	@Test
	void contextLoads() {
	}

	@Test
	void shouldPersistData() {
		UrlMappingEntity entity = new UrlMappingEntity();
		entity.setChecksum(1);
		entity.setUrl("www.example.com/path?query=values");
		entity.setShortUrl("aaaaa");
		entity.setInsertTs(new Date(Instant.now().getEpochSecond()));

		BigInteger id = urlMappingService.persistEntity(entity);
		assertEquals(BigInteger.ONE, id);

		Optional<UrlMappingEntity> savedEntity = urlMappingService.fetchEntity(id);
		assertTrue(savedEntity.isPresent());
		savedEntity.ifPresent(
				fetchedEntity -> {
					assertEquals(1, fetchedEntity.getChecksum());
					assertEquals("www.example.com/path?query=values", fetchedEntity.getUrl());
					assertEquals("aaaaa", fetchedEntity.getShortUrl());
					assertNotNull(fetchedEntity.getId());
					assertTrue(BigInteger.ZERO.compareTo(fetchedEntity.getId()) < 0);
					assertNotNull(fetchedEntity.getInsertTs());
					assertTrue(fetchedEntity.getVersion() > 0);
				}
		);
	}

}
