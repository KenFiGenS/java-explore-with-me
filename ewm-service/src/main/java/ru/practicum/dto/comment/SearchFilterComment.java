package ru.practicum.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

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
    @JsonFormat(pattern = PATTERN_FOR_DATETIME)
    private LocalDateTime start;
    @JsonFormat(pattern = PATTERN_FOR_DATETIME)
    private LocalDateTime end;
}
