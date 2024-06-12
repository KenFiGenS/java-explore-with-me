package ru.practicum.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDtoForCreate {
    private int id;
    private List<Integer> events;
    private Boolean pinned;
    private String title;
}
