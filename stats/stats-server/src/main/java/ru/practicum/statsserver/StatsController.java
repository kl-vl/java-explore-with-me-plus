package ru.practicum.statsserver;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.statsdto.EndpointHitDto;
import ru.practicum.statsdto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatsController {

    private final StatsService service;

    @GetMapping("/stats")
    public List<ViewStatsDto> getStats(@Valid @RequestParam LocalDateTime start,
                                       @Valid @RequestParam LocalDateTime end,
                                       @Valid @RequestParam(required = false) String[] uris,
                                       @Valid @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        return service.getStat(start, end, uris, unique);
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public boolean saveHit(@Valid @RequestBody EndpointHitDto endpointHitDto) {
        try {
            service.addStat(endpointHitDto);
            return true;
        } catch (Exception e) {
            log.warn("Save hint uri = {} from ip = {} exception {}",
                    endpointHitDto.getUri(),
                    endpointHitDto.getIp(),
                    e.getMessage(),
                    e);
        }
        return false;
    }
}
