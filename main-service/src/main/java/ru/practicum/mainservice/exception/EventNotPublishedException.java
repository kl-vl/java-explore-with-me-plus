package ru.practicum.mainservice.exception;

public class EventNotPublishedException extends Exception {
    public EventNotPublishedException(String message) {
        super(message);
    }
}
