package ru.practicum.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Controllers-DTO для передачи сведений об ошибке
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ProblemDetail {
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSSxxx");
    private final String timestamp;
    private final int status;
    private final String error;
    private final String path;
    @Nullable
    private String message;
    @Nullable
    private Map<String, Object> properties;

    public static ProblemDetail withCustomError(HttpStatus httpStatus, String path, String error) {
        ZoneId zoneUtc = ZoneId.of("UTC");
        return new ProblemDetail(ZonedDateTime.now(zoneUtc).format(ISO_FORMATTER), httpStatus.value(), error, path);
    }

    public ProblemDetail(HttpStatus httpStatus, String path, @Nullable String message) {
        this(httpStatus, path);
        this.message = message;
    }

    public ProblemDetail(HttpStatus httpStatus, String path) {
        ZoneId zoneUtc = ZoneId.of("UTC");
        this.timestamp = ZonedDateTime.now(zoneUtc).format(ISO_FORMATTER);
        this.status = httpStatus.value();
        this.error = httpStatus.getReasonPhrase();
        this.path = path;
    }

    public void setProperty(String name, @Nullable Object value) {
        this.properties = this.properties != null ? this.properties : new LinkedHashMap<>();
        this.properties.put(name, value);
    }
}
