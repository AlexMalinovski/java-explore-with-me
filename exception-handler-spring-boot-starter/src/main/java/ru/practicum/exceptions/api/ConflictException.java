package ru.practicum.exceptions.api;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
