package pt.teixeiram2.UrlShortner.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import pt.teixeiram2.UrlShortner.converter.BiDirectionalConverter;
import pt.teixeiram2.UrlShortner.model.UrlMap;
import pt.teixeiram2.UrlShortner.model.UrlMappingEntity;
import pt.teixeiram2.UrlShortner.repository.UrlMappingRepository;

import java.sql.Date;
import java.time.Instant;
import java.util.Optional;

@Service
public class UrlMappingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UrlMappingService.class);

    private final UrlMappingRepository urlMappingRepository;
    private final ShortUrlGenerator shortUrlGenerator;
    private final BiDirectionalConverter<UrlMap, UrlMappingEntity> urlMapConverter;

    public UrlMappingService(UrlMappingRepository urlMappingRepository,
                             ShortUrlGenerator shortUrlGenerator,
                             BiDirectionalConverter<UrlMap, UrlMappingEntity> urlMapConverter) {
        this.urlMappingRepository = urlMappingRepository;
        this.shortUrlGenerator = shortUrlGenerator;
        this.urlMapConverter = urlMapConverter;
    }

    public UrlMap createMapping(String url) {
        long checksum = shortUrlGenerator.computeChecksum(url);

        Optional<UrlMappingEntity> urlMapOptional = urlMappingRepository.findByChecksum(checksum);
        if (urlMapOptional.isPresent()) {
            LOGGER.info("operation=createMapping, checksum={}, msg='Fetched pre-existing url'", checksum);
            return urlMapConverter.convertTo(urlMapOptional.get());
        } else {
            UrlMap urlMap = shortUrlGenerator.shortenUrl(url, checksum);
            UrlMappingEntity entity = urlMapConverter.convertFrom(urlMap);
            entity.setInsertTs(new Date(Instant.now().toEpochMilli()));
            try {
                urlMappingRepository.save(entity);
            } catch (Exception ex) {
                if (ex instanceof DataIntegrityViolationException) {
                    LOGGER.info("operation=createMapping, checksum={}, Possible race condition, retry fetching", checksum);
                    urlMappingRepository.findByChecksum(checksum)
                            .map(urlMapConverter::convertTo)
                            .orElseThrow(() -> new RuntimeException("Unable to persist entity"));
                } else {
                    throw new RuntimeException("Unable to persist entity");
                }
            }
            return urlMap;
        }
    }

    public Optional<UrlMap> fetchFullUrl(String shortUrl) {
        return urlMappingRepository.findByShortUrl(shortUrl)
                .map(urlMapConverter::convertTo);
    }

}
