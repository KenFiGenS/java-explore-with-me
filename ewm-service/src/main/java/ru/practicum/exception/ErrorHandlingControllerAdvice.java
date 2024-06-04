package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

import static ru.practicum.constant.Constant.DATE_TIME_FORMATTER;

@RestControllerAdvice
@Slf4j
public class ErrorHandlingControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Violation onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.info("Получен статус 400 BAD_REQUEST {}", e.getMessage(), e);
        return new Violation(e.getFieldError().toString(),
                "BAD_REQUEST",
                "Incorrectly made request.",
                "Failed: " + e.getFieldError().getField() + ". Error: " + e.getFieldError().getDefaultMessage() + ". Value: null",
                LocalDateTime.now().format(DATE_TIME_FORMATTER));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Violation onDataInsertValidException(DataIntegrityViolationException e) {
        log.info("Получен статус 409 CONFLICT {}", e.getMessage(), e);
        return new Violation(e.getStackTrace().toString(),
                "CONFLICT",
                "Integrity constraint has been violated.",
                e.getMessage(),
                LocalDateTime.now().format(DATE_TIME_FORMATTER));
    }
}
