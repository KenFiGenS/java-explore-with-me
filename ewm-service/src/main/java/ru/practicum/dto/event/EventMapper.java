package ru.practicum.dto.event;

import lombok.experimental.UtilityClass;
import ru.practicum.model.category.Category;
import ru.practicum.model.user.User;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.EventStatus;
import ru.practicum.model.event.Location;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

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
                eventDtoCreate.getPaid() == null ? false : eventDtoCreate.getPaid(),
                eventDtoCreate.getEventDate(),
                user,
                eventDtoCreate.getDescription(),
                eventDtoCreate.getParticipantLimit(),
                EventStatus.PENDING,
                LocalDateTime.now(),
                null,
                eventDtoCreate.getLocation().getLat(),
                eventDtoCreate.getLocation().getLon(),
                eventDtoCreate.getRequestModeration() == null ? true : eventDtoCreate.getRequestModeration(),
                0,
                new ArrayList<>()
        );
    }

    public static EventDtoForResponse toEventDtoForResponse(Event event) {
        return new EventDtoForResponse(
                event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                event.getCategory(),
                event.isPaid(),
                event.getEventDate(),
                event.getInitiator(),
                event.getDescription(),
                event.getParticipantLimit(),
                event.getState() == null ? EventStatus.PENDING : event.getState(),
                event.getCreatedOn(),
                event.getPublishedOn() !=null ? event.getPublishedOn() : null,
                new Location(event.getLat(), event.getLon()),
                event.isRequestModeration(),
                event.getConfirmedRequests(),
                0
        );
    }

    public static EventDtoForShortResponse toEventDtoForShortResponse(Event event) {
        return new EventDtoForShortResponse(
                event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                event.getCategory(),
                event.isPaid(),
                event.getEventDate(),
                event.getInitiator(),
                event.getConfirmedRequests(),
                0
        );
    }
}
