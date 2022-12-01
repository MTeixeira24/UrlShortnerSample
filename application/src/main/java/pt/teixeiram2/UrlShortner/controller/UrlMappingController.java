package pt.teixeiram2.UrlShortner.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pt.teixeiram2.UrlShortner.model.UrlMap;
import pt.teixeiram2.UrlShortner.service.UrlMappingService;

@Controller
public class UrlMappingController {

    private final UrlMappingService urlMappingService;

    public UrlMappingController(UrlMappingService urlMappingService) {
        this.urlMappingService = urlMappingService;
    }

    //TODO Dummy implementation for testing
    @PostMapping(path = "/createMapping")
    public @ResponseBody UrlMap createMapping(@RequestParam String url) {
        return urlMappingService.createMapping(url);
    }

}
