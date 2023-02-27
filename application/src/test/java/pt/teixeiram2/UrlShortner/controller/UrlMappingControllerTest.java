package pt.teixeiram2.UrlShortner.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import pt.teixeiram2.UrlShortner.controller.exception.InvalidRequest;
import pt.teixeiram2.UrlShortner.controller.exception.NoSuchMappingException;
import pt.teixeiram2.UrlShortner.dto.CreateMappingRequest;
import pt.teixeiram2.UrlShortner.dto.CreateMappingResponse;
import pt.teixeiram2.UrlShortner.dto.FetchMappingResponse;
import pt.teixeiram2.UrlShortner.dto.InvalidRequestResponse;
import pt.teixeiram2.UrlShortner.model.UrlMap;
import pt.teixeiram2.UrlShortner.service.UrlMappingService;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

//TODO Use MVC slice testing
@ExtendWith(MockitoExtension.class)
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
        victim = new UrlMappingControllerMock(urlMappingService);
    }

    @Test
    @Disabled
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

        ResponseEntity<CreateMappingResponse> response = victim.createMapping(request);
        Mockito.verify(urlMappingService, Mockito.times(1)).createMapping(url);
        assertEquals(expected, response);
    }

    @Test
    @Disabled
    public void shouldFetchUrl() {
        String url = "example.com";
        String shortUrl = "aaaa";
        FetchMappingResponse expected = new FetchMappingResponse(url);
        UrlMap urlMap = UrlMap.builder()
                .withChecksum(1111L)
                .withShortUrl(shortUrl)
                .withUrl(url)
                .build();

        Mockito.when(urlMappingService.fetchFullUrl(shortUrl))
                .thenReturn(Optional.of(urlMap));

        FetchMappingResponse response = victim.getFullUrl(shortUrl);
        Mockito.verify(urlMappingService, Mockito.times(1)).fetchFullUrl(shortUrl);
        assertNotNull(response);
        assertEquals(expected, response);
    }

    @Test
    @Disabled
    public void shouldThrowIfMappingNotFound() {
        String shortUrl = "aaaa";

        Mockito.when(urlMappingService.fetchFullUrl(shortUrl))
                .thenReturn(Optional.empty());

        assertThrows(NoSuchMappingException.class, () -> victim.getFullUrl(shortUrl));

        Mockito.verify(urlMappingService, Mockito.times(1)).fetchFullUrl(shortUrl);
    }

    @ParameterizedTest
    @NullSource
    @MethodSource("illegalArgsProvider")
    public void shouldThrowOnInvalidArgs(CreateMappingRequest createMappingRequest) {
        assertThrows(InvalidRequest.class, () -> victim.createMapping(createMappingRequest));
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void shouldThrowOnInvalidParamsWhenFetchingUrl(String input) {
        assertThrows(InvalidRequest.class, () -> victim.getFullUrl(input));
    }

    @Test
    @Disabled
    public void shouldHandleInvalidMappingRequests() {
        RuntimeException ex = new InvalidRequest("Error Message");
        ResponseEntity<InvalidRequestResponse> response = victim.invalidRequestHandlerDelegate(ex, null);
        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Error Message", response.getBody().getCause());
        assertEquals(InvalidRequestResponse.INVALID_REQUEST_ERROR, response.getBody().getError());
    }

    @Test
    @Disabled
    public void shouldHandleNotFoundMappings() {
        RuntimeException ex = new NoSuchMappingException("Error Message");
        ResponseEntity<InvalidRequestResponse> response = victim.noSuchMappingHandlerDelegate(ex, null);
        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Error Message", response.getBody().getCause());
        assertEquals(InvalidRequestResponse.NO_SUCH_MAPPING_ERROR, response.getBody().getError());
    }

    static class UrlMappingControllerMock extends UrlMappingController {

        public UrlMappingControllerMock(UrlMappingService urlMappingService) {
            super(urlMappingService, "a");
        }

        public ResponseEntity<InvalidRequestResponse> invalidRequestHandlerDelegate(RuntimeException ex, WebRequest request) {
            return super.invalidRequestHandler(ex, request);
        }

        public ResponseEntity<InvalidRequestResponse> noSuchMappingHandlerDelegate(RuntimeException ex, WebRequest request) {
            return super.noSuchMappingHandler(ex, request);
        }
    }

}