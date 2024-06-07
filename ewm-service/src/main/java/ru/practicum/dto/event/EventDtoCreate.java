package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.user.User;
import ru.practicum.model.event.Location;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDtoCreate {
    private int id;
    @NotBlank
    private String title;
    @NotBlank
    private String annotation;
    @Positive
    private int category;
    private boolean paid;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Future
    private LocalDateTime eventDate;
    private User initiator;
    @NotBlank
    private String description;
    @Positive
    private int participantLimit;
    @NotNull
    private Location location;
    private boolean requestModeration;
}
