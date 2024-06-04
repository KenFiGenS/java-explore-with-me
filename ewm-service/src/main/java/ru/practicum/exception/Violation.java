package ru.practicum.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Violation {
    private String error;
    private String status;
    private String reason;
    private String massage;
    private String timeStamp;
}
