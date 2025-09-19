package ru.practicum.mainservice.exception;

public class CategoryIsRelatedToEventException extends Exception {
    public CategoryIsRelatedToEventException(String message) {
        super(message);
    }
}
