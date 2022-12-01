package pt.teixeiram2.UrlShortner.service;

import org.springframework.stereotype.Service;
import pt.teixeiram2.UrlShortner.model.UrlMappingEntity;
import pt.teixeiram2.UrlShortner.repository.UrlMappingRepository;

import java.math.BigInteger;
import java.util.Optional;

@Service
public class UrlMappingService {

    private final UrlMappingRepository urlMappingRepository;

    public UrlMappingService(UrlMappingRepository urlMappingRepository) {
        this.urlMappingRepository = urlMappingRepository;
    }

    //TODO Dummy implementation for testing
    public BigInteger persistEntity(UrlMappingEntity urlMapping) {
        return urlMappingRepository.save(urlMapping).getId();
    }

    //TODO Dummy implementation for testing
    public Optional<UrlMappingEntity> fetchEntity(BigInteger id) {
        return urlMappingRepository.findById(id);
    }

}
