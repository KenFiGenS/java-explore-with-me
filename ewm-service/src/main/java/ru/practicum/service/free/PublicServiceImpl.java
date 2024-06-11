package ru.practicum.service.free;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.HitClient;
import ru.practicum.StatsClient;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.CategoryMapper;
import ru.practicum.dto.event.EventDtoForResponse;
import ru.practicum.dto.event.EventMapper;
import ru.practicum.model.category.Category;
import ru.practicum.model.event.Event;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.statsDto.StatsDtoCreate;
import ru.practicum.statsDto.StatsDtoWithHitsCount;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        String app = request.getRemoteAddr();
        String uri = request.getRequestURI();
        String ip = request.getRemoteAddr();
        List<StatsDtoWithHitsCount> stats = statsClient.getStats(
                null,
                null,
                Arrays.asList(uri),
                false
                );
        EventDtoForResponse response = EventMapper.toEventDtoForResponse(currentEvent);
        response.setViews(stats.get(0).getHits());
        hitClient.createHit(new StatsDtoCreate(0, app, uri, ip, LocalDateTime.now()));
        return response;
    }
}
