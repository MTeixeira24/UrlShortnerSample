package pt.teixeiram2.UrlShortner.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pt.teixeiram2.UrlShortner.model.UrlMappingEntity;
import pt.teixeiram2.UrlShortner.service.UrlMappingService;

import java.sql.Date;
import java.time.Instant;

@Controller
public class UrlMappingController {

    private final UrlMappingService urlMappingService;

    public UrlMappingController(UrlMappingService urlMappingService) {
        this.urlMappingService = urlMappingService;
    }

    //TODO Dummy implementation for testing
    @PostMapping(path = "/createMapping")
    public @ResponseBody String createMapping(@RequestParam String url) {
        UrlMappingEntity entity = new UrlMappingEntity();
        entity.setInsertTs(new Date(Instant.now().toEpochMilli()));
        entity.setUrl(url);
        entity.setChecksum(url.hashCode());
        entity.setShortUrl("aaaa");
        urlMappingService.persistEntity(entity);
        return "aaaa";
    }

}
