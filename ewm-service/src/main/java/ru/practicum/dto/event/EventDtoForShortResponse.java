package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.category.Category;
import ru.practicum.model.user.User;

import java.time.LocalDateTime;

import static ru.practicum.constant.Constant.PATTERN_FOR_DATETIME;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDtoForShortResponse {
    private int id;
    private String title;
    private String annotation;
    private Category category;
    private boolean paid;
    @JsonFormat(pattern = PATTERN_FOR_DATETIME)
    private LocalDateTime eventDate;
    private User initiator;
    private int confirmedRequests;
    private int views;
}
