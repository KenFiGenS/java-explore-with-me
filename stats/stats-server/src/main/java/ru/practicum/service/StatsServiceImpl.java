package ru.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.model.Hit;
import ru.practicum.model.HitMapper;
import ru.practicum.model.SearchFilter;
import ru.practicum.repository.HitsRepository;
import ru.practicum.statsDto.StatsDtoCreate;
import ru.practicum.statsDto.StatsDtoWithHitsCount;

import java.time.LocalDateTime;
import java.util.*;
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
        SearchFilter searchFilter = new SearchFilter(start, end, uris);
        System.out.println(searchFilter);
        List<Specification<Hit>> specifications = eventFilterToSpecification(searchFilter);
        List<Hit> hitsByFilter = hitsRepository.findAll(specifications.stream().reduce(Specification::or).orElse(null));
        System.out.println(hitsRepository.findAll());
        System.out.println(hitsByFilter);
        Set<StatsDtoWithHitsCount> response = new HashSet<>();
        for (Hit hit : hitsByFilter) {
            if (unique) {
                response.add(new StatsDtoWithHitsCount(hit.getApp(), hit.getUri(), (int) hitsByFilter.stream()
                        .filter(h -> h.getUri().equals(hit.getUri()))
                        .map(Hit::getIp)
                        .distinct()
                        .count()));
            } else {
                response.add(new StatsDtoWithHitsCount(hit.getApp(), hit.getUri(), (int) hitsByFilter.stream().filter(h -> h.getUri().equals(hit.getUri())).count()));
            }
        }
        return response.stream().sorted(Comparator.comparing(StatsDtoWithHitsCount::getHits).reversed()).collect(Collectors.toList());
    }

    private List<Specification<Hit>> eventFilterToSpecification(SearchFilter filter) {
        List<Specification<Hit>> specifications = new ArrayList<>();
        specifications.add(filter.getStart() == null ? null : rangeGrater(filter.getStart()));
        specifications.add(filter.getEnd() == null ? null : rangeLess(filter.getEnd()));
        specifications.add(filter.getUris().get(0).equals("all") ? null : urisIn(filter.getUris()));
        return specifications.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    private Specification<Hit> rangeLess(LocalDateTime rangeEnd) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("timestamp"), rangeEnd));
    }

    private Specification<Hit> rangeGrater(LocalDateTime rangeStart) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("timestamp"), rangeStart));
    }

    private Specification<Hit> urisIn(List<String> uris) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("uri")).value(uris);
    }
}
