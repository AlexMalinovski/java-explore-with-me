package ru.practicum.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.practicum.exceptions.api.BadRequestException;
import ru.practicum.exceptions.api.ConflictException;
import ru.practicum.exceptions.api.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Object> notFoundExceptionHandler(NotFoundException ex, WebRequest request) {
        ProblemDetail problemDetail = new ProblemDetail(
                HttpStatus.NOT_FOUND,
                ((ServletWebRequest) request).getRequest().getRequestURI(),
                ex.getMessage());
        log.trace(ex.getMessage());
        return handleExceptionInternal(ex, problemDetail, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler
    public ResponseEntity<Object> conflictExceptionHandler(ConflictException ex, WebRequest request) {
        ProblemDetail problemDetail = new ProblemDetail(
                HttpStatus.CONFLICT,
                ((ServletWebRequest) request).getRequest().getRequestURI(),
                ex.getMessage());
        log.trace(ex.getMessage());
        return handleExceptionInternal(ex, problemDetail, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler
    public ResponseEntity<Object> badRequestExceptionHandler(BadRequestException ex, WebRequest request) {
        ProblemDetail problemDetail = new ProblemDetail(
                HttpStatus.BAD_REQUEST,
                ((ServletWebRequest) request).getRequest().getRequestURI(),
                ex.getMessage());
        log.trace(ex.getMessage());
        return handleExceptionInternal(ex, problemDetail, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        ProblemDetail problemDetail = new ProblemDetail(
                HttpStatus.BAD_REQUEST,
                ((ServletWebRequest) request).getRequest().getRequestURI(),
                "Ошибка валидации");
        Map<String, String> errors = ex.getConstraintViolations()
                .stream()
                .collect(Collectors.toMap(
                        f -> f.getPropertyPath().toString(),
                        f -> f.getMessage() != null ? f.getMessage() : ""));
        problemDetail.setProperty("errors", errors);
        log.warn(ex.getMessage());
        return handleExceptionInternal(ex, problemDetail, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleAllUncaughtException(RuntimeException ex, WebRequest request) {
        ProblemDetail problemDetail = new ProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ((ServletWebRequest) request).getRequest().getRequestURI());
        log.error(ex.getMessage());
        return handleExceptionInternal(ex, problemDetail, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatus status,
                                                                  @NonNull WebRequest request) {
        ProblemDetail problemDetail = new ProblemDetail(
                HttpStatus.BAD_REQUEST,
                ((ServletWebRequest) request).getRequest().getRequestURI(),
                "Ошибка валидации");
        Map<String, String> errors = ex.getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        f -> f.getDefaultMessage() != null ? f.getDefaultMessage() : ""));
        problemDetail.setProperty("errors", errors);
        log.warn(ex.getMessage());
        return handleExceptionInternal(ex, problemDetail, headers, status, request);
    }
}
