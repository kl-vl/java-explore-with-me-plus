package ru.practicum.statsserver;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.statsdto.EndpointHitDto;
import ru.practicum.statsdto.ViewStatsDto;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatsController {

    //private final StatService statService;

    @GetMapping("/stats")
    public List<ViewStatsDto> getStats(@RequestParam String start,
                                       @RequestParam String end,
                                       @RequestParam(required = false) List<String> uris,
                                       @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        // TODO реализация
        //List<ViewStatsDto> stats = statService.getStat(start, end, uris, unique);
        //return stats;
        return Collections.emptyList();
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public boolean saveHit(@RequestBody EndpointHitDto endpointHitDto) {
        // TODO реализация
        // statService.addStat(endpointHitDto);
        return false;
    }
}
