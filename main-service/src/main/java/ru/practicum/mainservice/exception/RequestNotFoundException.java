package ru.practicum.mainservice.exception;

public class RequestNotFoundException extends Exception {
    public RequestNotFoundException(String message) {
        super(message);
    }
}
