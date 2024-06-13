package ru.practicum.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDtoForCreate {
    private int id;
    private List<Integer> events;
    private Boolean pinned;
    @NotBlank
    @Length(max = 50)
    private String title;
}
