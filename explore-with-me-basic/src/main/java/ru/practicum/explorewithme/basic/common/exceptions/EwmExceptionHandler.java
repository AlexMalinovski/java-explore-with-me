package ru.practicum.explorewithme.basic.common.exceptions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import ru.practicum.exceptions.ApiExceptionHandler;
import ru.practicum.exceptions.ProblemDetail;
import ru.practicum.exceptions.api.NotFoundException;

import java.util.Map;
import java.util.Optional;


@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class EwmExceptionHandler extends ApiExceptionHandler {

    private final Map<String, ErrorData> dbConstraintsErr;

    @ExceptionHandler
    public ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest request) {
        ErrorData errorData = null;
        if (ex.getCause() instanceof ConstraintViolationException) {
            String constraint = ((ConstraintViolationException) ex.getCause()).getConstraintName();
            errorData = dbConstraintsErr.getOrDefault(constraint, null);
        }

        if (errorData != null) {
            return handleByErrorData(ex, errorData, request);
        }
        return handleAllUncaught(ex, request);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleEmptyResultDataAccess(EmptyResultDataAccessException ex, WebRequest request) {
        return handleNotFound(new NotFoundException(ex.getMessage()), request);
    }

    private ResponseEntity<Object> handleByErrorData(Exception ex, ErrorData errorData, WebRequest request) {
        ProblemDetail problemDetail = new ProblemDetail(
                errorData.getHttpStatus(),
                ((ServletWebRequest) request).getRequest().getRequestURI(),
                Optional.ofNullable(errorData.getMessage()).orElse(ex.getMessage()));
        log.trace(ex.getMessage());
        return handleExceptionInternal(ex, problemDetail, new HttpHeaders(), errorData.getHttpStatus(), request);
    }
}
