package pt.teixeiram2.UrlShortner.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pt.teixeiram2.UrlShortner.controller.exception.InvalidRequest;
import pt.teixeiram2.UrlShortner.controller.exception.NoSuchMappingException;
import pt.teixeiram2.UrlShortner.dto.CreateMappingRequest;
import pt.teixeiram2.UrlShortner.dto.CreateMappingResponse;
import pt.teixeiram2.UrlShortner.dto.FetchMappingResponse;
import pt.teixeiram2.UrlShortner.dto.InvalidRequestResponse;
import pt.teixeiram2.UrlShortner.model.UrlMap;
import pt.teixeiram2.UrlShortner.service.UrlMappingService;

import java.net.URI;

@RestController
@RequestMapping(path = "/shorten")
public class UrlMappingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UrlMappingController.class);

    private final UrlMappingService urlMappingService;
    private final String redirectServiceDomain;

    public UrlMappingController(UrlMappingService urlMappingService,
                                @Value("${ust.redirectServiceDomain}") String redirectServiceDomain) {
        this.urlMappingService = urlMappingService;
        this.redirectServiceDomain = redirectServiceDomain;
    }

    @PostMapping(path = "/createMapping")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CreateMappingResponse> createMapping(@RequestBody CreateMappingRequest request) {
        if (request == null || request.getFullUrl() == null || request.getFullUrl().isBlank()) {
            LOGGER.error("operation=createMapping, request={}, msg='Received invalid mapping request'", request);
            throw new InvalidRequest("Invalid url");
        }

        UrlMap urlMap = urlMappingService.createMapping(request.getFullUrl());

        URI location = ServletUriComponentsBuilder
                .fromHttpUrl(redirectServiceDomain)
                .path("/{shortUrl}")
                .buildAndExpand(urlMap.getShortUrl())
                .toUri();

        CreateMappingResponse createMappingResponse = CreateMappingResponse.builder()
                .withShortUrl(urlMap.getShortUrl())
                .withUrl(urlMap.getUrl())
                .build();

        return ResponseEntity
                .created(location)
                .body(createMappingResponse);

    }

    @GetMapping(path = "/getFullUrl")
    public FetchMappingResponse getFullUrl(@RequestParam String shortUrl) {
        if (shortUrl == null || shortUrl.isBlank()) {
            throw new InvalidRequest("Invalid url provided");
        }

        return urlMappingService.fetchFullUrl(shortUrl)
                .map(UrlMap::getUrl)
                .map(FetchMappingResponse::new)
                .orElseThrow(() -> new NoSuchMappingException("Mapping not Found"));
    }

    @ExceptionHandler(InvalidRequest.class)
    protected ResponseEntity<InvalidRequestResponse> invalidRequestHandler(RuntimeException ex, WebRequest request) {
        InvalidRequestResponse invalidRequestResponse =
                new InvalidRequestResponse(InvalidRequestResponse.INVALID_REQUEST_ERROR, ex.getMessage());
        return ResponseEntity
                .badRequest()
                .headers(new HttpHeaders())
                .body(invalidRequestResponse);
    }

    @ExceptionHandler(NoSuchMappingException.class)
    protected ResponseEntity<InvalidRequestResponse> noSuchMappingHandler(RuntimeException ex, WebRequest request) {
        InvalidRequestResponse invalidRequestResponse =
                new InvalidRequestResponse(InvalidRequestResponse.NO_SUCH_MAPPING_ERROR, ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(invalidRequestResponse);
    }

}
