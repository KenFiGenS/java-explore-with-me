package ru.practicum.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.dto.event.EventDtoForShortResponse;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComplicationDtoForResponse {
    private int id;
    private List<EventDtoForShortResponse> events;
    private Boolean pinned;
    private String title;
}
