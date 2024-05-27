package ru.practicum.model;

import ru.practicum.statsDto.StatsDtoCreate;

public class HitMapper {

    public static Hit toHit(StatsDtoCreate statsDtoCreate) {
        return new Hit(
                statsDtoCreate.getId(),
                statsDtoCreate.getApp(),
                statsDtoCreate.getUri(),
                statsDtoCreate.getIp(),
                statsDtoCreate.getTimestamp()
        );
    }

    public static StatsDtoCreate toStatsDtoCreate(Hit hit) {
        return new StatsDtoCreate(
                hit.getId(),
                hit.getApp(),
                hit.getUri(),
                hit.getIp(),
                hit.getTimestamp()
        );
    }
}
