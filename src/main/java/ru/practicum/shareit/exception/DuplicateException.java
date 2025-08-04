package ru.practicum.shareit.exception;

public class DuplicateException extends RuntimeException {
    public DuplicateException(String message) {
        super(message);
    }

    public DuplicateException(String message, Object... args) {
        super(String.format(message.replace("{}", "%s"), args));
    }
}