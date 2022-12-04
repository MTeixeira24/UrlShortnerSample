package pt.teixeiram2.UrlShortner.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import pt.teixeiram2.UrlShortner.controller.exception.InvalidMappingRequest;
import pt.teixeiram2.UrlShortner.dto.CreateMappingRequest;
import pt.teixeiram2.UrlShortner.dto.CreateMappingResponse;
import pt.teixeiram2.UrlShortner.dto.InvalidRequestResponse;
import pt.teixeiram2.UrlShortner.model.UrlMap;
import pt.teixeiram2.UrlShortner.service.UrlMappingService;

import java.util.Optional;

@RestController
@RequestMapping(path = "/shorten")
public class UrlMappingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UrlMappingController.class);

    private final UrlMappingService urlMappingService;

    public UrlMappingController(UrlMappingService urlMappingService) {
        this.urlMappingService = urlMappingService;
    }

    //TODO Dummy implementation for testing
    @PostMapping(path = "/createMapping")
    public CreateMappingResponse createMapping(@RequestBody CreateMappingRequest request) {
        if (request == null || request.getFullUrl() == null || request.getFullUrl().isBlank()) {
            LOGGER.error("operation=createMapping, request={}, msg='Received invalid mapping request'", request);
            throw new InvalidMappingRequest("Invalid url");
        }

        UrlMap urlMap = urlMappingService.createMapping(request.getFullUrl());
        return CreateMappingResponse.builder()
                .withShortUrl(urlMap.getShortUrl())
                .withUrl(urlMap.getUrl())
                .build();
    }

    @GetMapping(path = "/getFullUrl")
    public String getFullUrl(@RequestParam String shortUrl) {
        Optional<String> fullUrl = urlMappingService.fetchFullUrl(shortUrl);
        return fullUrl.orElse("Not Found");
    }

    @ExceptionHandler(InvalidMappingRequest.class)
    protected ResponseEntity<InvalidRequestResponse> invalidRequestHandler(RuntimeException ex, WebRequest request) {
        InvalidRequestResponse invalidRequestResponse =
                new InvalidRequestResponse(InvalidRequestResponse.INVALID_REQUEST_ERROR, ex.getMessage());
        return ResponseEntity
                .badRequest()
                .headers(new HttpHeaders())
                .body(invalidRequestResponse);
    }

}
