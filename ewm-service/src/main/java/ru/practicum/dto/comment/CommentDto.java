package ru.practicum.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static ru.practicum.constant.Constant.PATTERN_FOR_DATETIME;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private int id;
    private int event;
    private int author;
    private String text;
    @JsonFormat(pattern = PATTERN_FOR_DATETIME)
    private LocalDateTime createdOn;
}
