package ru.practicum.stats.client;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import ru.practicum.stats.ClientRestStatImpl;
import ru.practicum.stats.EndpointHitDto;
import ru.practicum.stats.ViewStatsDto;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClientRestStatImplIntegrationTest {

    private MockWebServer mockWebServer;

    private ClientRestStatImpl client;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        RestClient restClient = RestClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        client = new ClientRestStatImpl(restClient);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void addStatShouldSendCorrectPostRequest() throws InterruptedException {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("true"));

        EndpointHitDto dto = new EndpointHitDto();
        dto.setApp("ewm-main-service");
        dto.setUri("/events/1");
        dto.setIp("192.168.1.1");
        dto.setTimestamp(LocalDateTime.of(2023, 1, 1, 12, 0));

        Boolean result = client.addStat(dto);

        assertTrue(result);

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/hit", recordedRequest.getPath());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, recordedRequest.getHeader(HttpHeaders.CONTENT_TYPE));
        assertTrue(recordedRequest.getBody().readUtf8().contains("\"app\":\"ewm-main-service\""));
    }

    @Test
    void getStatShouldSendCorrectGetRequestAndParseResponse() throws InterruptedException {
        String responseBody = "[" +
                "{\"app\": \"ewm-main-service\", \"uri\": \"/events/1\", \"hits\": 15}," +
                "{\"app\": \"ewm-main-service\", \"uri\": \"/events/2\", \"hits\": 8}" +
                "]";

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(responseBody));

        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 12, 30, 45);
        LocalDateTime end = LocalDateTime.of(2023, 1, 2, 15, 45, 30);
        List<String> uris = List.of("/events/1", "/events/2");

        List<ViewStatsDto> result = client.getStat(start, end, uris, true);

        assertNotNull(result);
        assertEquals(2, result.size());

        ViewStatsDto firstStat = result.getFirst();
        assertEquals("ewm-main-service", firstStat.getApp());
        assertEquals("/events/1", firstStat.getUri());
        assertEquals(15L, firstStat.getHits());

        ViewStatsDto secondStat = result.get(1);
        assertEquals("ewm-main-service", secondStat.getApp());
        assertEquals("/events/2", secondStat.getUri());
        assertEquals(8L, secondStat.getHits());

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());
        assertNotNull(recordedRequest.getRequestUrl());
        String requestUrl = recordedRequest.getRequestUrl().toString();

        System.out.println("Request URL: " + requestUrl);

        assertTrue(requestUrl.contains("/stats"));
        assertTrue(requestUrl.contains("start=2023-01-01%2012:30:45") ||
                requestUrl.contains("start=2023-01-01+12:30:45"));
        assertTrue(requestUrl.contains("end=2023-01-02%2015:45:30") ||
                requestUrl.contains("end=2023-01-02+15:45:30"));
        assertTrue(requestUrl.contains("unique=true"));
        assertTrue(requestUrl.contains("uris=/events/1"));
        assertTrue(requestUrl.contains("uris=/events/2"));
    }

    @Test
    void getStatWithNullUrisShouldBuildUrlWithoutUris() throws InterruptedException {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("[]"));

        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 1, 2, 0, 0);

        List<ViewStatsDto> result = client.getStat(start, end, Collections.emptyList(), false);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertNotNull(recordedRequest.getRequestUrl());
        String requestUrl = recordedRequest.getRequestUrl().toString();

        String decodedUrl = java.net.URLDecoder.decode(requestUrl, StandardCharsets.UTF_8);

        assertTrue(decodedUrl.contains("/stats"));
        assertTrue(decodedUrl.contains("start=2023-01-01 00:00:00"));
        assertTrue(decodedUrl.contains("end=2023-01-02 00:00:00"));
        assertTrue(decodedUrl.contains("unique=false"));
        assertFalse(decodedUrl.contains("uris="));
    }

    @Test
    void getStatWithEmptyUrisShouldBuildUrlWithoutUris() throws InterruptedException {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("[]"));

        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 1, 2, 0, 0);
        List<String> uris = List.of();

        List<ViewStatsDto> result = client.getStat(start, end, uris, true);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertNotNull(recordedRequest.getRequestUrl());
        String requestUrl = recordedRequest.getRequestUrl().toString();

        String decodedUrl = java.net.URLDecoder.decode(requestUrl, StandardCharsets.UTF_8);

        assertTrue(decodedUrl.contains("/stats"));
        assertTrue(decodedUrl.contains("start=2023-01-01 00:00:00"));
        assertTrue(decodedUrl.contains("end=2023-01-02 00:00:00"));
        assertTrue(decodedUrl.contains("unique=true"));
        assertFalse(decodedUrl.contains("uris="));
    }

    @Test
    void getStatWhenServerReturnsErrorShouldThrowException() throws InterruptedException {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setBody("Internal Server Error"));

        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 1, 2, 0, 0);
        List<String> uris = List.of("/events/1");

        assertThrows(org.springframework.web.client.HttpServerErrorException.class, () -> client.getStat(start, end, uris, true));

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());
    }

    @Test
    void getStatWhenServerReturnsNullBodyShouldReturnEmptyList() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(""));

        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 1, 2, 0, 0);
        List<String> uris = List.of("/events/1");

        List<ViewStatsDto> result = client.getStat(start, end, uris, true);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}