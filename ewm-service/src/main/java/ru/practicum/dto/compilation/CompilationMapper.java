package ru.practicum.dto.compilation;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.event.EventDtoForShortResponse;
import ru.practicum.model.compilation.Compilation;
import ru.practicum.model.event.Event;

import java.util.List;

@UtilityClass
public class CompilationMapper {

    public static Compilation toCompilation(List<Event> events, CompilationDtoForCreate compilationDto) {
        return new Compilation(
                compilationDto.getId(),
                events,
                compilationDto.getPinned() != null && compilationDto.getPinned(),
                compilationDto.getTitle()
        );
    }

    public static ComplicationDtoForResponse toComplicationDtoForResponse(List<EventDtoForShortResponse> events, Compilation compilation) {
        return new ComplicationDtoForResponse(
                compilation.getId(),
                events,
                compilation.isPinned(),
                compilation.getTitle()
        );
    }
}
