package ru.practicum.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.model.event.EventStatus;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.constant.Constant.PATTERN_FOR_DATETIME;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchFilterForAdmin {
    private List<Integer> users;
    private List<EventStatus> states;
    private List<Integer> categories;
    @DateTimeFormat(pattern = PATTERN_FOR_DATETIME)
    private LocalDateTime rangeStart;
    @DateTimeFormat(pattern = PATTERN_FOR_DATETIME)
    private LocalDateTime rangeEnd;
}
