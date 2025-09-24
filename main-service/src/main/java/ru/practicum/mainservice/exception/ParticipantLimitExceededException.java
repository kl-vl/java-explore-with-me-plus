package ru.practicum.mainservice.exception;

public class ParticipantLimitExceededException extends Exception {
    public ParticipantLimitExceededException(String message) {
        super(message);
    }
}
