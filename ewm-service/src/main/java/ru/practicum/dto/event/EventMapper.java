package ru.practicum.dto.event;

import lombok.experimental.UtilityClass;
import ru.practicum.model.Category;
import ru.practicum.model.User;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.EventStatus;
import ru.practicum.model.event.Location;

import java.time.LocalDateTime;

@UtilityClass
public class EventMapper {

    public static Event toEvent(EventDtoCreate eventDtoCreate,
                                Category category,
                                User user) {
        return new Event(
                0,
                eventDtoCreate.getTitle(),
                eventDtoCreate.getAnnotation(),
                category,
                eventDtoCreate.isPaid(),
                eventDtoCreate.getEventDate(),
                user,
                eventDtoCreate.getDescription(),
                eventDtoCreate.getParticipantLimit(),
                EventStatus.WAITING,
                LocalDateTime.now(),
                eventDtoCreate.getLocation().getLat(),
                eventDtoCreate.getLocation().getLon(),
                eventDtoCreate.isRequestModeration()
        );
    }

    public static EventDtoAfterCreate toEventDtoAfterCreate(Event event) {
        return new EventDtoAfterCreate(
                event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                event.getCategory(),
                event.isPaid(),
                event.getEventDate(),
                event.getInitiator(),
                event.getDescription(),
                event.getParticipantLimit(),
                EventStatus.WAITING,
                event.getCreatedOn(),
                new Location(event.getLat(), event.getLon()),
                event.isRequestModeration(),
                0,
                0
        );
    }
}
