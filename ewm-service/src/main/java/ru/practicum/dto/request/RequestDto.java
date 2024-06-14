package ru.practicum.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.request.RequestStatus;

import java.time.LocalDateTime;

import static ru.practicum.constant.Constant.PATTERN_FOR_DATETIME;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto {
    private int id;
    @JsonFormat(pattern = PATTERN_FOR_DATETIME)
    private LocalDateTime created;
    private int event;
    private int requester;
    private RequestStatus status;
}
