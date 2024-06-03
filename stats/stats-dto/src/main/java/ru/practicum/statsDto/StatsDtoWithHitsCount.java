package ru.practicum.statsDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatsDtoWithHitsCount {
    private String app;
    private String uri;
    private int hits;
}
