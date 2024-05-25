package ru.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.model.Hit;
import ru.practicum.model.HitMapper;
import ru.practicum.repository.HitsRepository;
import ru.practicum.statsDto.StatsDtoCreate;
import ru.practicum.statsDto.StatsDtoWithHitsCount;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatsServiceImpl implements StatsService{
    @Autowired
    private HitsRepository hitsRepository;

    @Override
    public StatsDtoCreate create(StatsDtoCreate statsDtoCreate) {
        return HitMapper.toStatsDtoCreate(hitsRepository.save(HitMapper.toHit(statsDtoCreate)));
    }

    @Override
    public List<StatsDtoWithHitsCount> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        List<Hit> allHits = hitsRepository.findByTimestampIsAfterAndTimestampIsBefore(start, end);
        List<StatsDtoWithHitsCount> result = new ArrayList<>();
        if (!unique) {
            for (Hit currentHit : allHits) {
                result.add(new StatsDtoWithHitsCount("ewm-main-service",
                                currentHit.getUri(),
                                (int) allHits.stream().filter(h -> uris.contains(currentHit.getUri())).count()
                        )
                );
            }
        } else {
            for (Hit currentHit : allHits) {
                result.add(new StatsDtoWithHitsCount("ewm-main-service",
                                currentHit.getUri(),
                                (int) allHits.stream().filter(h -> uris.contains(currentHit.getUri()))
                                        .map(Hit::getUri)
                                        .distinct()
                                        .count()
                        )
                );
            }
        }
        return result;
    }
}
