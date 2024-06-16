package ru.practicum.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.constant.Constant.PATTERN_FOR_DATETIME;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchFilterComment {
    private Integer eventId;
    private Integer authorId;
    private List<Integer> commentsId;
    @DateTimeFormat(pattern = PATTERN_FOR_DATETIME)
    private LocalDateTime start;
    @DateTimeFormat(pattern = PATTERN_FOR_DATETIME)
    private LocalDateTime end;
}
