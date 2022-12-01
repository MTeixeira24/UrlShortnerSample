package pt.teixeiram2.UrlShortner.repository;

import org.springframework.data.repository.CrudRepository;
import pt.teixeiram2.UrlShortner.model.UrlMappingEntity;

import java.math.BigInteger;
import java.util.Optional;

public interface UrlMappingRepository extends CrudRepository<UrlMappingEntity, BigInteger> {
    Optional<UrlMappingEntity> findByChecksum(long checksum);
}
