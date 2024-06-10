package ru.practicum.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDtoAfterChangeStatus {
    List<RequestDto> confirmedRequests = new ArrayList<>();
    List<RequestDto> rejectedRequests = new ArrayList<>();
}
