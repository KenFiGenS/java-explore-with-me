package ru.practicum.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.event.EventDtoForResponse;
import ru.practicum.service.free.PublicService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@Validated
@RequestMapping
public class FreePublicController {
    @Autowired
    PublicService publicService;

    @GetMapping("/categories")
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") int from,
                                           @RequestParam(defaultValue = "10") int size) {
        log.info("PUBLIC: Выполняется запрос на получение страницы категорий, начиная с {} элемента, размером станицы в {} элементов", from, size);
        return publicService.getCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategoriesById(@PathVariable int catId) {
        log.info("PUBLIC: Выполняется запрос на получение категории под id: {}", catId);
        return publicService.getCategoriesById(catId);
    }

    @GetMapping("/events/{id}")
    public EventDtoForResponse getEventById(@PathVariable int id,
                                            HttpServletRequest request) {
        String app = request.getRemoteAddr();
        String uri = request.getRequestURI();
        String ip = request.getRemoteAddr();
        log.info("PUBLIC: Выполняется запрос на получение события под id: {}, app: {}, uri: {}, ip: {}", id, app, uri, ip);
        return publicService.getEventById(id, request);
    }
}
