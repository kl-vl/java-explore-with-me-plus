package ru.practicum.mainservice.exception;

public class RequestAlreadyExistsException extends Exception {
    public RequestAlreadyExistsException(String message) {
        super(message);
    }
}
