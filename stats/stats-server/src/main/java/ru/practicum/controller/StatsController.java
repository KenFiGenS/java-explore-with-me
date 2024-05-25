package ru.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.StatsService;
import ru.practicum.statsDto.StatsDtoCreate;
import ru.practicum.statsDto.StatsDtoWithHitsCount;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
public class StatsController {
    @Autowired
    StatsService service;

    @PostMapping("/hit")
    public StatsDtoCreate createHit(@RequestBody StatsDtoCreate statsDtoCreate) {
        return service.create(statsDtoCreate);
    }

    @GetMapping("/stats")
    public List<StatsDtoWithHitsCount> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                                @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                                @RequestParam(defaultValue = "null") List<String> uris,
                                                @RequestParam(defaultValue = "false") boolean unique) {
        log.info("Запрос статистики с параметрами: Период {} - {}, по uri {}, unique {}", start, end, uris, unique);
        return service.getStats(start, end, uris, unique);
    }
}