package ru.practicum.dto.request;

import lombok.experimental.UtilityClass;
import ru.practicum.model.request.Request;
import ru.practicum.model.request.RequestStatus;

import java.time.LocalDateTime;

@UtilityClass
public class RequestMapper {

    public static Request toRequest(int userId, int requestId) {
        return new Request(
                0,
                LocalDateTime.now(),
                requestId,
                userId,
                RequestStatus.PENDING
        );
    }

    public static RequestDto toRequestDto (Request request) {
        return new RequestDto(
                request.getId(),
                request.getCreated(),
                request.getEvent(),
                request.getRegister(),
                request.getStatus()
        );
    }
}
