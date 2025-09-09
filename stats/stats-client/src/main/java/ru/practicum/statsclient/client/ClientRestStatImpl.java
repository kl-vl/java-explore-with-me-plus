package ru.practicum.statsclient.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;
import ru.practicum.statsdto.EndpointHitDto;
import ru.practicum.statsdto.ViewStatsDto;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ClientRestStatImpl implements ClientRestStat {

    private final RestClient restClient;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ClientRestStatImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public EndpointHitDto addStat(EndpointHitDto dto) {
        return restClient.post()
                .uri("/hit")
                .body(dto)
                .retrieve()
                .body(EndpointHitDto.class);
    }

    @Override
    public List<ViewStatsDto> getStat(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique) {
        ResponseEntity<ViewStatsDto[]> responseEntity = restClient.get()
                .uri(uriBuilder -> buildStatsUri(uriBuilder, start, end, uris, unique))
                .retrieve()
                .toEntity(ViewStatsDto[].class);

        return responseEntity.getBody() != null ? Arrays.asList(responseEntity.getBody()) : Collections.emptyList();
    }

    private URI buildStatsUri(UriBuilder uriBuilder,
                              LocalDateTime start,
                              LocalDateTime end,
                              String[] uris,
                              boolean unique) {
        UriBuilder builder = uriBuilder
                .path("/stats")
                .queryParam("start", formatDateTime(start))
                .queryParam("end", formatDateTime(end))
                .queryParam("unique", unique);

        if (uris != null) {
            for (String uri : uris) {
                builder.queryParam("uris", uri);
            }
        }

        return builder.build();
    }

    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(FORMATTER);
    }
}