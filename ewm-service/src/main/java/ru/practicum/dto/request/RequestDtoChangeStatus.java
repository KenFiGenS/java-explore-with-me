package ru.practicum.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.request.RequestStatus;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDtoChangeStatus {
    List<Integer> requestIds;
    RequestStatus status;
}
