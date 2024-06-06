package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.Category;
import ru.practicum.model.User;
import ru.practicum.model.event.EventStatus;
import ru.practicum.model.event.Location;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDtoAfterCreate {
    private int id;
    private String title;
    private String annotation;
    private Category category;
    private boolean paid;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private User initiator;
    private String description;
    private int participantLimit;
    private EventStatus state;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    private Location location;
    private boolean requestModeration;
    private int confirmedRequests;
    private int views;
}
