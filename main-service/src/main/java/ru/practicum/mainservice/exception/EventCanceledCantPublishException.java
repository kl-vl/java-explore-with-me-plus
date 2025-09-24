package ru.practicum.mainservice.exception;

public class EventCanceledCantPublishException extends Exception {
    public EventCanceledCantPublishException(String message) {
        super(message);
    }
}
