package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.model.event.Location;

import javax.validation.constraints.Future;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDtoUserUpdate {
    private int id;
    @Length(min = 3, max = 120)
    private String title;
    @Length(min = 20, max = 2000)
    private String annotation;
    private int category;
    @Length(min = 20, max = 7000)
    private String description;
    @Future
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    @PositiveOrZero
    private int participantLimit;
    private Boolean requestModeration;
    private StateActionForUser stateAction;
}
