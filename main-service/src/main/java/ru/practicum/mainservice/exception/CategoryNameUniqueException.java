package ru.practicum.mainservice.exception;

public class CategoryNameUniqueException extends Exception {
    public CategoryNameUniqueException(String message) {
        super(message);
    }
}
