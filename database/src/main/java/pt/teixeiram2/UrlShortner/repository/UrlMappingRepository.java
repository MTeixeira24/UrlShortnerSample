package pt.teixeiram2.UrlShortner.repository;

import org.springframework.data.repository.CrudRepository;
import pt.teixeiram2.UrlShortner.model.UrlMappingEntity;

import java.math.BigInteger;

public interface UrlMappingRepository extends CrudRepository<UrlMappingEntity, BigInteger> {

}
