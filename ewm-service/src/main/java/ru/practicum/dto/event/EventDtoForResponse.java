package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.category.Category;
import ru.practicum.model.user.User;
import ru.practicum.model.event.EventStatus;
import ru.practicum.model.event.Location;

import java.time.LocalDateTime;

import static ru.practicum.constant.Constant.PATTERN_FOR_DATETIME;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDtoForResponse {
    private int id;
    private String title;
    private String annotation;
    private Category category;
    private boolean paid;
    @JsonFormat(pattern = PATTERN_FOR_DATETIME)
    private LocalDateTime eventDate;
    private User initiator;
    private String description;
    private int participantLimit;
    private EventStatus state;
    @JsonFormat(pattern = PATTERN_FOR_DATETIME)
    private LocalDateTime createdOn;
    @JsonFormat(pattern = PATTERN_FOR_DATETIME)
    private LocalDateTime publishedOn;
    private Location location;
    private boolean requestModeration;
    private int confirmedRequests;
    private int views;
}
