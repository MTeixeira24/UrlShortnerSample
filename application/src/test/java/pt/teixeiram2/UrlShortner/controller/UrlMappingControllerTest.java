package pt.teixeiram2.UrlShortner.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import pt.teixeiram2.UrlShortner.controller.exception.InvalidMappingRequest;
import pt.teixeiram2.UrlShortner.dto.CreateMappingRequest;
import pt.teixeiram2.UrlShortner.dto.CreateMappingResponse;
import pt.teixeiram2.UrlShortner.dto.InvalidRequestResponse;
import pt.teixeiram2.UrlShortner.model.UrlMap;
import pt.teixeiram2.UrlShortner.service.UrlMappingService;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class UrlMappingControllerTest {

    private UrlMappingControllerMock victim;

    @Mock
    private UrlMappingService urlMappingService;

    public static Stream<Arguments> illegalArgsProvider() {
        CreateMappingRequest blankRequest = new CreateMappingRequest();
        blankRequest.setFullUrl("  ");
        return Stream.of(
                Arguments.of(new CreateMappingRequest()),
                Arguments.of(blankRequest)
        );
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        victim = new UrlMappingControllerMock(urlMappingService);
    }

    @Test
    public void shouldReturnMappingResponse() {
        String url = "example.com";
        String shortUrl = "aaaa";
        UrlMap urlMap = UrlMap.builder()
                .withChecksum(1111L)
                .withShortUrl(shortUrl)
                .withUrl(url)
                .build();
        CreateMappingRequest request = new CreateMappingRequest();
        request.setFullUrl(url);
        CreateMappingResponse expected = new CreateMappingResponse(url, shortUrl);

        Mockito.when(urlMappingService.createMapping(url))
                .thenReturn(urlMap);

        CreateMappingResponse response = victim.createMapping(request);
        Mockito.verify(urlMappingService, Mockito.times(1)).createMapping(url);
        assertEquals(expected, response);
    }


    @ParameterizedTest
    @NullSource
    @MethodSource("illegalArgsProvider")
    public void shouldThrowOnInvalidArgs(CreateMappingRequest createMappingRequest) {
        assertThrows(InvalidMappingRequest.class, () -> victim.createMapping(createMappingRequest));
    }

    @Test
    public void shouldHandleInvalidMappingRequests() {
        RuntimeException ex = new InvalidMappingRequest("Error Message");
        ResponseEntity<InvalidRequestResponse> response = victim.invalidRequestHandlerDelegate(ex, null);
        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Error Message", response.getBody().getCause());
        assertEquals(InvalidRequestResponse.INVALID_REQUEST_ERROR, response.getBody().getError());
    }

    static class UrlMappingControllerMock extends UrlMappingController {

        public UrlMappingControllerMock(UrlMappingService urlMappingService) {
            super(urlMappingService);
        }

        public ResponseEntity<InvalidRequestResponse> invalidRequestHandlerDelegate(RuntimeException ex, WebRequest request) {
            return super.invalidRequestHandler(ex, request);
        }
    }

}