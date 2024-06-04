package ru.practicum.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchFilter {
    private List<Integer> ids;
    private int from;
    private int size;
}
