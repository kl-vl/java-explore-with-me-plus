package ru.practicum.mainservice.handler;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import ru.practicum.mainservice.exception.CategoryIsRelatedToEventException;
import ru.practicum.mainservice.exception.CategoryNameUniqueException;
import ru.practicum.mainservice.exception.CategoryNotFoundException;
import ru.practicum.mainservice.exception.CompilationNotFoundException;
import ru.practicum.mainservice.exception.EventAlreadyPublishedException;
import ru.practicum.mainservice.exception.EventCanceledCantPublishException;
import ru.practicum.mainservice.exception.EventDateException;
import ru.practicum.mainservice.exception.EventNotFoundException;
import ru.practicum.mainservice.exception.EventValidationException;
import ru.practicum.mainservice.exception.UserAlreadyExistsException;
import ru.practicum.stats.ErrorResponseDto;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    private String getStackTraceAsString(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        return stringWriter.toString();
    }

    private String extractParameterName(String propertyPath) {
        if (propertyPath.contains(".")) {
            String[] parts = propertyPath.split("\\.");
            return parts[parts.length - 1];
        }
        return propertyPath;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleValidationExceptions(final MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> Optional.ofNullable(fieldError.getDefaultMessage()).orElse("Invalid value")
                ));

        log.warn("Method argument validation error in {} : {}", request.getDescription(false), errors, ex);

        return new ErrorResponseDto("Validation failed", "VALIDATION_ERROR", errors);
    }

    @ExceptionHandler(EventDateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto errorHandlerEventDate(final Exception ex, final WebRequest request) {

        log.error("Event date not valid {}: {}", request.getDescription(false), ex.getMessage(), ex);

        Map<String, String> details = new HashMap<>();
        details.put("exception", ex.getClass().getSimpleName());
        details.put("message", ex.getMessage());

        return new ErrorResponseDto("Event date not valid", "BAD_REQUEST", details);
    }

    @ExceptionHandler({CategoryNotFoundException.class, EventNotFoundException.class, CompilationNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto errorHandlerNotFound(final Exception ex, final WebRequest request) {

        log.error("Entity not found in {}: {}", request.getDescription(false), ex.getMessage(), ex);

        Map<String, String> details = new HashMap<>();
        details.put("exception", ex.getClass().getSimpleName());
        details.put("message", ex.getMessage());

        return new ErrorResponseDto("Entity not found", "NOT_FOUND", details);
    }

    @ExceptionHandler({CategoryNameUniqueException.class, EventValidationException.class, UserAlreadyExistsException.class,
            CategoryIsRelatedToEventException.class, EventAlreadyPublishedException.class, EventCanceledCantPublishException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseDto errorHandlerCategoryNameUnique(final Exception ex, final WebRequest request) {

        log.error("Category not found in {}: {}", request.getDescription(false), ex.getMessage(), ex);

        Map<String, String> details = new HashMap<>();
        details.put("exception", ex.getClass().getSimpleName());
        details.put("message", ex.getMessage());

        return new ErrorResponseDto("Category name must be unique", "CONFLICT", details);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        Map<String, String> errors = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> extractParameterName(violation.getPropertyPath().toString()),
                        violation -> Optional.ofNullable(violation.getMessage()).orElse("Invalid value")
                ));

        log.warn("Constraint violation error in {} : {}", request.getDescription(false), errors, ex);

        return new ErrorResponseDto("Validation failed", "VALIDATION_ERROR", errors);
    }

}