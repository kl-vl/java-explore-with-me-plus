package ru.practicum.stats;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class ClientRestStatImpl implements ClientRestStat {

    private final RestClient restClient;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ClientRestStatImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public Boolean addStat(EndpointHitDto dto) {
        return restClient.post()
                .uri("/hit")
                .body(dto)
                .retrieve()
                .body(Boolean.class);
    }

    @Override
    public List<ViewStatsDto> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        URI uri = buildStatsUri(start, end, uris, unique);

        ResponseEntity<ViewStatsDto[]> responseEntity = restClient.get()
                .uri(uri)
                .retrieve()
                .toEntity(ViewStatsDto[].class);

        return responseEntity.getBody() != null ? Arrays.asList(responseEntity.getBody()) : Collections.emptyList();
    }

    private URI buildStatsUri(LocalDateTime start,
                              LocalDateTime end,
                              List<String> uris,
                              boolean unique) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/stats")
                .queryParam("start", formatDateTime(start))
                .queryParam("end", formatDateTime(end))
                .queryParam("unique", unique);

        if (uris != null && !uris.isEmpty()) {
            for (String uri : uris) {
                builder.queryParam("uris", uri);
            }
        }
        return builder.build().toUri();
    }

    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(FORMATTER);
    }
}