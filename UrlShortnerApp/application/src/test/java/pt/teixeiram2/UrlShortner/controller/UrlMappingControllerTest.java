package pt.teixeiram2.UrlShortner.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pt.teixeiram2.UrlShortner.dto.CreateMappingRequest;
import pt.teixeiram2.UrlShortner.dto.CreateMappingResponse;
import pt.teixeiram2.UrlShortner.dto.FetchMappingResponse;
import pt.teixeiram2.UrlShortner.dto.InvalidRequestResponse;
import pt.teixeiram2.UrlShortner.model.UrlMap;
import pt.teixeiram2.UrlShortner.service.UrlMappingService;

import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UrlMappingController.class)
class UrlMappingControllerTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UrlMappingService urlMappingService;

    public static Stream<Arguments> illegalArgsProvider() {
        CreateMappingRequest blankRequest = new CreateMappingRequest();
        blankRequest.setFullUrl("  ");
        return Stream.of(
                Arguments.of(new CreateMappingRequest()),
                Arguments.of(blankRequest)
        );
    }

    @Test
    public void shouldReturnMappingResponse() throws Exception {

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

        given(urlMappingService.createMapping(url))
                .willReturn(urlMap);

        mockMvc.perform(post("/shorten/createMapping")
                    .content(OBJECT_MAPPER.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(OBJECT_MAPPER.writeValueAsString(expected)));
    }

    @Test
    public void shouldFetchUrl() throws Exception {
        String url = "example.com";
        String shortUrl = "aaaa";
        FetchMappingResponse expected = new FetchMappingResponse(url);
        UrlMap urlMap = UrlMap.builder()
                .withChecksum(1111L)
                .withShortUrl(shortUrl)
                .withUrl(url)
                .build();

        given(urlMappingService.fetchFullUrl(shortUrl))
                .willReturn(Optional.of(urlMap));

        mockMvc.perform(get("/shorten/getFullUrl?shortUrl={shortUrl}", shortUrl)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(OBJECT_MAPPER.writeValueAsString(expected)));
    }

    @Test
    public void shouldThrowIfMappingNotFound() throws Exception {
        String shortUrl = "aaaa";
        String expected = OBJECT_MAPPER.writeValueAsString(
                new InvalidRequestResponse(InvalidRequestResponse.NO_SUCH_MAPPING_ERROR, "Mapping not Found")
        );

        given(urlMappingService.fetchFullUrl(shortUrl))
                .willReturn(Optional.empty());

        mockMvc.perform(get("/shorten/getFullUrl?shortUrl={shortUrl}", shortUrl)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expected));
    }

    @ParameterizedTest
    @MethodSource("illegalArgsProvider")
    public void shouldThrowOnInvalidArgs(CreateMappingRequest createMappingRequest) throws Exception {

        mockMvc.perform(post("/shorten/createMapping")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createMappingRequest)))
                .andDo(print())
                .andExpect(content().json(OBJECT_MAPPER.writeValueAsString(
                        new InvalidRequestResponse(InvalidRequestResponse.INVALID_REQUEST_ERROR, "Invalid url")
                )));
    }

    private static String toJson(CreateMappingRequest createMappingRequest) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(createMappingRequest);
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void shouldThrowOnInvalidParamsWhenFetchingUrl(String input) throws Exception {
        mockMvc.perform(get("/shorten/getFullUrl?shortUrl={shortUrl}", input)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(OBJECT_MAPPER.writeValueAsString(
                        new InvalidRequestResponse(InvalidRequestResponse.INVALID_REQUEST_ERROR, "Invalid url provided")
                )));
    }

}