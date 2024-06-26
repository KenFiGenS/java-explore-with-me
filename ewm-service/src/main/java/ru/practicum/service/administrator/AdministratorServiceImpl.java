package ru.practicum.service.administrator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatsClient;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.CategoryMapper;
import ru.practicum.dto.compilation.CompilationDtoForCreate;
import ru.practicum.dto.compilation.CompilationDtoForUpdate;
import ru.practicum.dto.compilation.CompilationMapper;
import ru.practicum.dto.compilation.ComplicationDtoForResponse;
import ru.practicum.dto.event.*;
import ru.practicum.dto.user.UserDto;
import ru.practicum.dto.user.UserMapper;
import ru.practicum.model.category.Category;
import ru.practicum.model.compilation.Compilation;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.EventStatus;
import ru.practicum.model.user.User;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.ComplicationRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.statsDto.StatsDtoWithHitsCount;

import java.sql.SQLDataException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AdministratorServiceImpl implements AdministratorService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private ComplicationRepository complicationRepository;
    @Autowired
    private StatsClient statsClient;

    @Override
    public UserDto createUser(UserDto userDtoCreate) {
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDtoCreate)));
    }

    @Override
    public List<UserDto> getUsers(List<Integer> ids, int from, int size) {
        if (!ids.isEmpty()) {
            List<Specification<User>> specifications = new ArrayList<>();
            specifications.add((root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("id")).value(ids));
            return userRepository.findAll(specifications.stream().reduce(Specification::or).orElse(null)).stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        } else {
            int currentPage = from / size;
            Pageable pageable = PageRequest.of(currentPage, size);
            return userRepository.findAll(pageable).stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        }
    }

    @SneakyThrows
    @Override
    public void removeUser(int id) {
        try {
            User currentUser = userRepository.getReferenceById(id);
            userRepository.delete(currentUser);
        } catch (RuntimeException e) {
            throw new SQLDataException(e.getMessage());
        }
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        return CategoryMapper.toCategoryDto(categoryRepository.save(CategoryMapper.toCategory(categoryDto)));
    }


    @Override
    public void removeCategory(int catId) {
        Category currentCategory = categoryRepository.getReferenceById(catId);
        categoryRepository.deleteById(catId);

    }

    @Override
    public CategoryDto updateCategory(int catId, CategoryDto categoryDto) {
        categoryDto.setId(catId);
        Category currentCategory = categoryRepository.getReferenceById(catId);
        if (currentCategory.getName().equals(categoryDto.getName())) return categoryDto;
        return CategoryMapper.toCategoryDto(categoryRepository.save(CategoryMapper.toCategory(categoryDto)));
    }

    @Transactional
    @Override
    public EventDtoForResponse updateEvent(int eventId, EventDtoAdminUpdate eventDtoAdminUpdate) {
        Event eventForUpdate = eventRepository.getReferenceById(eventId);
        if (eventDtoAdminUpdate.getAnnotation() != null)
            eventForUpdate.setAnnotation(eventDtoAdminUpdate.getAnnotation());
        if (eventDtoAdminUpdate.getCategory() != 0) {
            Category categoryForUpdate = categoryRepository.getReferenceById(eventDtoAdminUpdate.getCategory());
            eventForUpdate.setCategory(categoryForUpdate);
        }
        if (eventDtoAdminUpdate.getDescription() != null)
            eventForUpdate.setDescription(eventDtoAdminUpdate.getDescription());
        if (eventDtoAdminUpdate.getEventDate() != null) {
            if (eventForUpdate.getEventDate().isAfter(eventForUpdate.getCreatedOn().plusHours(1))) {
                eventForUpdate.setEventDate(eventDtoAdminUpdate.getEventDate());
            } else {
                throw new IllegalArgumentException("Время начала события менее чем через час");
            }
        }

        if (eventDtoAdminUpdate.getLocation() != null) {
            eventForUpdate.setLat(eventDtoAdminUpdate.getLocation().getLat());
            eventForUpdate.setLon(eventDtoAdminUpdate.getLocation().getLon());
        }
        if (eventDtoAdminUpdate.getPaid() != null) eventForUpdate.setPaid(eventDtoAdminUpdate.getPaid());
        if (eventDtoAdminUpdate.getParticipantLimit() != 0)
            eventForUpdate.setParticipantLimit(eventDtoAdminUpdate.getParticipantLimit());
        if (eventDtoAdminUpdate.getRequestModeration() != null)
            eventForUpdate.setRequestModeration(eventDtoAdminUpdate.getRequestModeration());
        if (eventDtoAdminUpdate.getStateAction() != null && eventDtoAdminUpdate.getStateAction().equals(StateActionForAdmin.PUBLISH_EVENT)) {
            if (eventForUpdate.getState().equals(EventStatus.PENDING)) {
                eventForUpdate.setState(EventStatus.PUBLISHED);
                eventForUpdate.setPublishedOn(LocalDateTime.now());
            } else if (eventForUpdate.getState().equals(EventStatus.PUBLISHED)) {
                throw new IllegalArgumentException("Cannot publish the event because it's not in the right state: PUBLISHED");
            } else {
                throw new IllegalArgumentException("Cannot publish the event because it's not in the right state: REJECTED");
            }
        } else {
            if (eventForUpdate.getState().equals(EventStatus.PUBLISHED)) {
                throw new IllegalArgumentException("Cannot rejected the event because it's not in the right state: PUBLISHED");
            } else {
                eventForUpdate.setState(EventStatus.REJECTED);
            }
        }
        if (eventDtoAdminUpdate.getTitle() != null) eventForUpdate.setTitle(eventDtoAdminUpdate.getTitle());
        return EventMapper.toEventDtoForResponse(eventRepository.save(eventForUpdate));
    }

    @Override
    public List<EventDtoForResponse> getAllEventBySpecification(SearchFilterForAdmin searchFilterForAdmin, int from, int size) {
        int currentPage = from / size;
        Pageable page = PageRequest.of(currentPage, size);
        List<Specification<Event>> specifications = eventFilterToSpecification(searchFilterForAdmin);
        return eventRepository.findAll(specifications.stream().reduce(Specification::and).orElse(null), page).stream()
                .sorted(Comparator.comparing(Event::getEventDate))
                .map(EventMapper::toEventDtoForResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ComplicationDtoForResponse createCompilation(CompilationDtoForCreate dtoForCreate) {
        if (dtoForCreate.getEvents() == null) {
            return new ComplicationDtoForResponse();
        }
        List<Event> eventsForCompilation = eventRepository.findAllById(dtoForCreate.getEvents());
        List<EventDtoForShortResponse> eventsDtoForCompilation = eventsForCompilation.stream()
                .map(EventMapper::toEventDtoForShortResponse)
                .collect(Collectors.toList());
        List<String> uris = eventsForCompilation.stream().map(e -> ("/event/" + e.getId())).collect(Collectors.toList());

        List<StatsDtoWithHitsCount> stats = statsClient.getStats(
                LocalDateTime.of(2024, 06, 12, 12, 25, 35),
                LocalDateTime.of(2025, 06, 12, 15, 25, 35),
                uris,
                true
        );
        ObjectMapper mapper = new ObjectMapper();
        List<StatsDtoWithHitsCount> statsAfterConvert = mapper.convertValue(stats, new TypeReference<>() {
        });
        for (EventDtoForShortResponse eventDto : eventsDtoForCompilation) {
            for (StatsDtoWithHitsCount statsDto : statsAfterConvert) {
                if (statsDto.getUri().equals("/event/" + eventDto.getId())) {
                    eventDto.setViews(statsDto.getHits());
                }
            }
        }
        return CompilationMapper.toComplicationDtoForResponse(eventsDtoForCompilation,
                complicationRepository.save(CompilationMapper.toCompilation(eventsForCompilation, dtoForCreate)));
    }

    @Override
    public ComplicationDtoForResponse updateCompilation(int compId, CompilationDtoForUpdate dtoForCreate) {
        Compilation complicationForUpdate = complicationRepository.getReferenceById(compId);
        if (dtoForCreate.getEvents() != null) {
            List<Event> eventsForCompilation = eventRepository.findAllById(dtoForCreate.getEvents());
            complicationForUpdate.setEvents(eventsForCompilation);
        }
        if (dtoForCreate.getPinned() != null) complicationForUpdate.setPinned(dtoForCreate.getPinned());
        if (dtoForCreate.getTitle() != null) complicationForUpdate.setTitle(dtoForCreate.getTitle());

        Compilation complicationAfterUpdate = complicationRepository.save(complicationForUpdate);
        List<EventDtoForShortResponse> eventsForCompilation = complicationAfterUpdate.getEvents().stream()
                .map(EventMapper::toEventDtoForShortResponse)
                .collect(Collectors.toList());

        List<String> uris = eventsForCompilation.stream().map(e -> ("/event/" + e.getId())).collect(Collectors.toList());

        List<StatsDtoWithHitsCount> stats = statsClient.getStats(
                LocalDateTime.of(2024, 06, 12, 12, 25, 35),
                LocalDateTime.of(2025, 06, 12, 15, 25, 35),
                uris,
                true
        );
        ObjectMapper mapper = new ObjectMapper();
        List<StatsDtoWithHitsCount> statsAfterConvert = mapper.convertValue(stats, new TypeReference<>() {
        });
        for (EventDtoForShortResponse eventDto : eventsForCompilation) {
            for (StatsDtoWithHitsCount statsDto : statsAfterConvert) {
                if (statsDto.getUri().equals("/event/" + eventDto.getId())) {
                    eventDto.setViews(statsDto.getHits());
                }
            }
        }
        return CompilationMapper.toComplicationDtoForResponse(eventsForCompilation, complicationAfterUpdate);
    }

    @Override
    public void removeCompilation(int compId) {
        complicationRepository.delete(complicationRepository.getReferenceById(compId));
    }

    private List<Specification<Event>> eventFilterToSpecification(SearchFilterForAdmin filter) {
        List<Specification<Event>> specifications = new ArrayList<>();
        specifications.add(filter.getUsers() == null ? null : idIn(filter.getUsers()));
        specifications.add(filter.getStates() == null ? null : stateIn(filter.getStates()));
        specifications.add(filter.getCategories() == null ? null : categoriesIn(filter.getCategories()));
        specifications.add(filter.getRangeStart() == null ? null : rangeGrater(filter.getRangeStart()));
        specifications.add(filter.getRangeEnd() == null ? null : rangeLess(filter.getRangeEnd()));
        return specifications.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    private Specification<Event> rangeLess(LocalDateTime rangeEnd) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("eventDate"), rangeEnd));
    }

    private Specification<Event> rangeGrater(LocalDateTime rangeStart) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("eventDate"), rangeStart));
    }

    private Specification<Event> categoriesIn(List<Integer> categories) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("category").get("id")).value(categories));
    }

    private Specification<Event> stateIn(List<EventStatus> states) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("state")).value(states);
    }

    private Specification<Event> idIn(List<Integer> values) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("initiator").get("id")).value(values));
    }
}
