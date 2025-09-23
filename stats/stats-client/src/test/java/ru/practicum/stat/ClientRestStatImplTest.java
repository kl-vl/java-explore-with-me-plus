package ru.practicum.stat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import ru.practicum.stats.ClientRestStatImpl;
import ru.practicum.stats.EndpointHitDto;
import ru.practicum.stats.ViewStatsDto;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientRestStatImplTest {

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private RestClient.RequestBodySpec requestBodySpec;

    @Mock
    private RestClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    private ClientRestStatImpl clientRestStat;

    private final LocalDateTime testStart = LocalDateTime.of(2024, 1, 1, 10, 0);
    private final LocalDateTime testEnd = LocalDateTime.of(2024, 1, 1, 12, 0);

    @BeforeEach
    void setUp() {
        clientRestStat = new ClientRestStatImpl(restClient);
    }

    @Test
    void addStat_ShouldReturnTrue_WhenRequestSuccessful() {
        EndpointHitDto dto = new EndpointHitDto();
        dto.setApp("test-app");
        dto.setUri("/test");
        dto.setIp("192.168.1.1");
        dto.setTimestamp(LocalDateTime.now());

        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("/hit")).thenReturn(requestBodySpec);
        when(requestBodySpec.body(dto)).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(Boolean.class)).thenReturn(true);

        Boolean result = clientRestStat.addStat(dto);

        assertTrue(result);
        verify(restClient).post();
        verify(requestBodyUriSpec).uri("/hit");
        verify(requestBodySpec).body(dto);
        verify(requestBodySpec).retrieve();
        verify(responseSpec).body(Boolean.class);
    }

    @Test
    void addStat_ShouldReturnFalse_WhenRequestFails() {
        EndpointHitDto dto = new EndpointHitDto();

        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("/hit")).thenReturn(requestBodySpec);
        when(requestBodySpec.body(dto)).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(Boolean.class)).thenReturn(false);

        Boolean result = clientRestStat.addStat(dto);

        assertFalse(result);
    }

    @Test
    void getStat_ShouldReturnStatsList_WhenUrisProvided() {
        List<String> uris = List.of("/events/1", "/events/2");
        ViewStatsDto stats1 = new ViewStatsDto("app1", "/events/1", 10L);
        ViewStatsDto stats2 = new ViewStatsDto("app1", "/events/2", 5L);
        ViewStatsDto[] statsArray = {stats1, stats2};

        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(URI.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(ViewStatsDto[].class))
                .thenReturn(ResponseEntity.ok(statsArray));

        List<ViewStatsDto> result = clientRestStat.getStat(testStart, testEnd, uris, true);

        assertEquals(2, result.size());
        assertEquals("/events/1", result.get(0).getUri());
        assertEquals("/events/2", result.get(1).getUri());
        verify(restClient).get();
        verify(requestHeadersUriSpec).uri(any(URI.class));
    }

    @Test
    void getStat_ShouldReturnStatsList_WhenNoUrisProvided() {
        ViewStatsDto stats = new ViewStatsDto("app1", "/events", 15L);
        ViewStatsDto[] statsArray = {stats};

        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(URI.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(ViewStatsDto[].class))
                .thenReturn(ResponseEntity.ok(statsArray));

        List<ViewStatsDto> result = clientRestStat.getStat(testStart, testEnd, Collections.emptyList(), false);

        assertEquals(1, result.size());
        assertEquals("/events", result.get(0).getUri());
        assertEquals(15L, result.get(0).getHits());
    }

    @Test
    void getStat_ShouldReturnEmptyList_WhenResponseBodyIsNull() {
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(URI.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(ViewStatsDto[].class))
                .thenReturn(ResponseEntity.ok(null));

        List<ViewStatsDto> result = clientRestStat.getStat(testStart, testEnd, List.of(), true);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getStat_ShouldReturnEmptyList_WhenServerError() {
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(URI.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(ViewStatsDto[].class))
                .thenReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null));

        List<ViewStatsDto> result = clientRestStat.getStat(testStart, testEnd, Collections.emptyList(), false);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}
