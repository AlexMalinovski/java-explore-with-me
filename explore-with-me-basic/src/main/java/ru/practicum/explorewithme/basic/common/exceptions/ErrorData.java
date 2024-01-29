package ru.practicum.explorewithme.basic.common.exceptions;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ErrorData {
    private final String message;
    private final HttpStatus httpStatus;

    public static ErrorData create(@Nullable String message, @NonNull HttpStatus httpStatus) {
        return new ErrorData(message, httpStatus);
    }
}
