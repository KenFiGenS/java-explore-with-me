package ru.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.model.Hit;
import ru.practicum.model.HitMapper;
import ru.practicum.repository.HitsRepository;
import ru.practicum.statsDto.StatsDtoCreate;
import ru.practicum.statsDto.StatsDtoWithHitsCount;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatsServiceImpl implements StatsService {
    @Autowired
    private HitsRepository hitsRepository;

    @Override
    public StatsDtoCreate create(StatsDtoCreate statsDtoCreate) {
        return HitMapper.toStatsDtoCreate(hitsRepository.save(HitMapper.toHit(statsDtoCreate)));
    }

    @Override
    public List<StatsDtoWithHitsCount> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        List<Hit> allHits = hitsRepository.findByTimestampIsAfterAndTimestampIsBefore(start, end);
        System.out.println(allHits);
        List<StatsDtoWithHitsCount> result = new ArrayList<>();
        if (!unique) {
            if (uris.get(0).equals("all")) {
                List<String> uniqueUri = allHits.stream().map(Hit::getUri).distinct().collect(Collectors.toList());
                for (String currentUri : uniqueUri) {
                    result.add(new StatsDtoWithHitsCount("ewm-main-service",
                                    currentUri,
                                    (int) allHits.stream().filter(h -> currentUri.equals(h.getUri())).count()
                            )
                    );
                }
            } else {
                for (String currentUri : uris) {
                    String trim1 = currentUri.replace("[", "");
                    String trim2 = trim1.replace("]", "");
                    result.add(new StatsDtoWithHitsCount("ewm-main-service",
                                    currentUri,
                                    (int) allHits.stream().filter(h -> trim2.equals(h.getUri())).count()
                            )
                    );
                }
            }
        } else {
            if (uris.get(0).equals("all")) {
                List<String> uniqueUri = allHits.stream().map(Hit::getUri).distinct().collect(Collectors.toList());
                for (String currentUri : uniqueUri) {
                    result.add(new StatsDtoWithHitsCount("ewm-main-service",
                                    currentUri,
                                    (int) allHits.stream().filter(h -> currentUri.equals(h.getUri()))
                                            .map(Hit::getIp)
                                            .distinct()
                                            .count()
                            )
                    );
                }
            } else {
                int count = 0;
                for (String currentUri : uris) {
                    String trim1 = currentUri.replace("[", "");
                    String trim2 = trim1.replace("]", "");
                    System.out.println(currentUri);
                    result.add(new StatsDtoWithHitsCount("ewm-main-service",
                                    currentUri,
                                    (int) allHits.stream().filter(h -> trim2.equals(h.getUri()))
                                            .map(Hit::getIp)
                                            .distinct()
                                            .count()
                            )
                    );
                }
            }
        }
        System.out.println(result);
        return result.stream().sorted(Comparator.comparing(StatsDtoWithHitsCount::getHits).reversed()).collect(Collectors.toList());
    }
}
