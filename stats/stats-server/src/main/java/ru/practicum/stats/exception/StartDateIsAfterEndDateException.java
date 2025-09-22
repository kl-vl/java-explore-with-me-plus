package ru.practicum.stats.exception;

public class StartDateIsAfterEndDateException extends Exception {
    public StartDateIsAfterEndDateException(String message) {
        super(message);
    }
}
