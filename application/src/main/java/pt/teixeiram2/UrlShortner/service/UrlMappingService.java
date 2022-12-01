package pt.teixeiram2.UrlShortner.service;

import org.springframework.stereotype.Service;
import pt.teixeiram2.UrlShortner.model.UrlMap;
import pt.teixeiram2.UrlShortner.model.UrlMappingEntity;
import pt.teixeiram2.UrlShortner.repository.UrlMappingRepository;

import java.sql.Date;
import java.time.Instant;
import java.util.Optional;

@Service
public class UrlMappingService {

    private final UrlMappingRepository urlMappingRepository;
    private final ShortUrlGenerator shortUrlGenerator;

    public UrlMappingService(UrlMappingRepository urlMappingRepository, ShortUrlGenerator shortUrlGenerator) {
        this.urlMappingRepository = urlMappingRepository;
        this.shortUrlGenerator = shortUrlGenerator;
    }

    //TODO Dummy implementation for testing
    public UrlMap createMapping(String url) {
        UrlMap urlMap = shortUrlGenerator.shortenUrl(url);
        UrlMappingEntity entity = new UrlMappingEntity();
        entity.setInsertTs(new Date(Instant.now().toEpochMilli()));
        entity.setUrl(urlMap.getUrl());
        entity.setChecksum(urlMap.getChecksum());
        entity.setShortUrl(urlMap.getShortUrl());
        urlMappingRepository.save(entity);
        return urlMap;
    }

    //TODO Dummy implementation for testing
    public Optional<UrlMappingEntity> fetchEntity(long checksum) {
        return urlMappingRepository.findByChecksum(checksum);
    }

}
