package ru.practicum.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Future;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.constant.Constant.PATTERN_FOR_DATETIME;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchFilterForPublic {
    private String text;
    private List<Integer> categories;
    private Boolean paid;
    @DateTimeFormat(pattern = PATTERN_FOR_DATETIME)
    private LocalDateTime rangeStart;
    @DateTimeFormat(pattern = PATTERN_FOR_DATETIME)
    @Future
    private LocalDateTime rangeEnd;
    private Boolean onlyAvailable;
}
