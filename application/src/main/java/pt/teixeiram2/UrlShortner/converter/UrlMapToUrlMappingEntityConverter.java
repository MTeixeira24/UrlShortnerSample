package pt.teixeiram2.UrlShortner.converter;

import org.springframework.stereotype.Component;
import pt.teixeiram2.UrlShortner.model.UrlMap;
import pt.teixeiram2.UrlShortner.model.UrlMappingEntity;

@Component("UrlMapToUrlMappingEntityConverter")
public class UrlMapToUrlMappingEntityConverter implements BiDirectionalConverter<UrlMap, UrlMappingEntity>  {

    @Override
    public UrlMap convertTo(UrlMappingEntity urlMappingEntity) {
        return UrlMap.builder()
                .withUrl(urlMappingEntity.getUrl())
                .withShortUrl(urlMappingEntity.getShortUrl())
                .withChecksum(urlMappingEntity.getChecksum())
                .build();
    }

    @Override
    public UrlMappingEntity convertFrom(UrlMap urlMap) {
        UrlMappingEntity urlMappingEntity = new UrlMappingEntity();
        urlMappingEntity.setShortUrl(urlMap.getShortUrl());
        urlMappingEntity.setUrl(urlMap.getUrl());
        urlMappingEntity.setChecksum(urlMap.getChecksum());
        return urlMappingEntity;
    }

}
