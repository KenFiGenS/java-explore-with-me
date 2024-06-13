package ru.practicum.service.free;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import ru.practicum.HitClient;
import ru.practicum.StatsClient;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.CategoryMapper;
import ru.practicum.dto.event.EventDtoForResponse;
import ru.practicum.dto.event.EventMapper;
import ru.practicum.dto.event.SearchFilterForPublic;
import ru.practicum.model.category.Category;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.EventStatus;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.statsDto.StatsDtoCreate;
import ru.practicum.statsDto.StatsDtoWithHitsCount;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.constant.Constant.DATE_TIME_FORMATTER;

@Service
public class PublicServiceImpl implements PublicService{
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    StatsClient statsClient;
    @Autowired
    HitClient hitClient;

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        int currentPage = from / size;
        Pageable pageable = PageRequest.of(currentPage, size);
        Page<Category> categories = categoryRepository.findAll(pageable);
        if (categories.isEmpty()) {
            return Arrays.asList();
        }
        return categories.stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoriesById(int catId) {
        return CategoryMapper.toCategoryDto(categoryRepository.getReferenceById(catId));
    }

    @Override
    public EventDtoForResponse getEventById(int id, HttpServletRequest request) {
        Event currentEvent = eventRepository.getReferenceById(id);
        if (!currentEvent.getState().equals(EventStatus.PUBLISHED)) {
            throw new EntityNotFoundException("Event with id=" + id + " was not found");
        }
        String app = "ewm-service";
        String uri = request.getRequestURI();
        String ip = request.getRemoteAddr();
        List<StatsDtoWithHitsCount> stats = statsClient.getStats(
                LocalDateTime.of(2024, 06, 12, 12, 25 , 35),
                LocalDateTime.of(2025, 06, 12, 15, 25 , 35),
                Arrays.asList(uri),
                true
                );
        ObjectMapper mapper = new ObjectMapper();
        List<StatsDtoWithHitsCount> stats1 = mapper.convertValue(stats, new TypeReference<>() {});
        EventDtoForResponse response = EventMapper.toEventDtoForResponse(currentEvent);
        if (stats != null && !stats.isEmpty()) {
            StatsDtoWithHitsCount statsDtoWithHitsCount = stats1.get(0);
            response.setViews(statsDtoWithHitsCount.getHits());
        }
        hitClient.createHit(new StatsDtoCreate(0, app, uri, ip, LocalDateTime.now()));
        return response;
    }

    @Override
    public List<EventDtoForResponse> getEventsBySearchFilter(SearchFilterForPublic filter,
                                                             String sort,
                                                             int from,
                                                             int size,
                                                             HttpServletRequest request) {
        int currentPage = from/size;
        Pageable page = PageRequest.of(currentPage, size);
        List<Specification<Event>> specifications = eventFilterToSpecification(filter);
        List<EventDtoForResponse> eventDtoListForResponse = eventRepository.findAll(specifications.stream().reduce(Specification::or).orElse(null), page).stream()
                .map(EventMapper::toEventDtoForResponse)
                .collect(Collectors.toList());

        List<String> uris = eventDtoListForResponse.stream().map(e -> ("/event/" + e.getId())).collect(Collectors.toList());
        String app = "ewm-service";
        String uri = request.getRequestURI();
        String ip = request.getRemoteAddr();

        List<StatsDtoWithHitsCount> stats = statsClient.getStats(
                LocalDateTime.of(2024, 06, 12, 12, 25 , 35),
                LocalDateTime.of(2025, 06, 12, 15, 25 , 35),
                List.of(uri),
                true
        );
        ObjectMapper mapper = new ObjectMapper();
        List<StatsDtoWithHitsCount> statsAfterConvert = mapper.convertValue(stats, new TypeReference<>() {});
        for (EventDtoForResponse eventDto : eventDtoListForResponse) {
            for (StatsDtoWithHitsCount statsDto : statsAfterConvert) {
                if (statsDto.getUri().equals("/event/" + eventDto.getId())) {
                    eventDto.setViews(statsDto.getHits());
                }
            }
        }
        hitClient.createHit(new StatsDtoCreate(0, app, uri, ip, LocalDateTime.now()));

        if (filter.getOnlyAvailable() != null && filter.getOnlyAvailable()) {
            eventDtoListForResponse.stream()
                    .filter(e -> e.getParticipantLimit() > e.getConfirmedRequests())
                    .collect(Collectors.toList());
        }

        if (sort.equals("EVENT_DATE")) {
            System.out.println(sort);
            return eventDtoListForResponse.stream()
                    .sorted(Comparator.comparing(EventDtoForResponse::getEventDate).reversed())
                    .collect(Collectors.toList());
        } else {
            return eventDtoListForResponse.stream()
                    .sorted(Comparator.comparing(EventDtoForResponse::getViews))
                    .collect(Collectors.toList());
        }
    }

    private List<Specification<Event>> eventFilterToSpecification(SearchFilterForPublic filter) {
        List<Specification<Event>> specifications = new ArrayList<>();
        specifications.add(filter.getText() == null ? null : textLikeAvailable(filter.getText()));
        specifications.add(filter.getText() == null ? null : textLikeDescription(filter.getText()));
        specifications.add(filter.getCategories() == null ? null : categoriesIn(filter.getCategories()));
        specifications.add(filter.getRangeStart() == null ? rangeGrater(LocalDateTime.now()) : rangeGrater(filter.getRangeStart()));
        specifications.add(filter.getRangeEnd() == null ? null : rangeLess(filter.getRangeEnd()));
        return specifications.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    private Specification<Event> textLikeAvailable(String text) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("annotation"), text));
    }

    private Specification<Event> textLikeDescription(String text) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("description"), text));
    }

    private Specification<Event> categoriesIn(List<Integer> categories) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("category").get("id")).value(categories));
    }

    private Specification<Event> rangeLess(LocalDateTime rangeEnd) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("eventDate"), rangeEnd));
    }

    private Specification<Event> rangeGrater(LocalDateTime rangeStart) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("eventDate"), rangeStart));
    }
}
