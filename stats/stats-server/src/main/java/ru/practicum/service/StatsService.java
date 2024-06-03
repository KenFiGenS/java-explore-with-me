package ru.practicum.service;

import ru.practicum.statsDto.StatsDtoCreate;
import ru.practicum.statsDto.StatsDtoWithHitsCount;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    StatsDtoCreate create(StatsDtoCreate statsDtoCreate);

    List<StatsDtoWithHitsCount> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
