package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.model.user.User;
import ru.practicum.model.event.Location;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

import static ru.practicum.constant.Constant.PATTERN_FOR_DATETIME;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDtoCreate {
    private int id;
    @NotBlank
    @Length(min = 3, max = 120)
    private String title;
    @NotBlank
    @Length(min = 20, max = 2000)
    private String annotation;
    @Positive
    private int category;
    private Boolean paid;
    @NotNull
    @JsonFormat(pattern = PATTERN_FOR_DATETIME)
    @Future
    private LocalDateTime eventDate;
    private User initiator;
    @NotBlank
    @Length(min = 20, max = 7000)
    private String description;
    @PositiveOrZero
    private int participantLimit;
    @NotNull
    private Location location;
    private Boolean requestModeration;
}
