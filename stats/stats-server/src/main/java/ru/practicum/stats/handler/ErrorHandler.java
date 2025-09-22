package ru.practicum.stats.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import ru.practicum.stats.ErrorResponseDto;
import ru.practicum.stats.exception.StartDateIsAfterEndDateException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

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

    @ExceptionHandler({MissingServletRequestParameterException.class,
            StartDateIsAfterEndDateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleIncorrectDataExceptions(Exception ex, WebRequest request) {
        log.error("Input data is incorrect {}: {}", request.getDescription(false), ex.getMessage(), ex);

        Map<String, String> details = new HashMap<>();
        details.put("exception", ex.getClass().getSimpleName());
        details.put("message", ex.getMessage());

        return new ErrorResponseDto("Input data is incorrect", "BAD_REQUEST", details);
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDto errorHandlerInternal(final Exception ex, final WebRequest request) {

        log.error("Internal error in {}: {}", request.getDescription(false), ex.getMessage(), ex);

        Map<String, String> details = new HashMap<>();
        details.put("exception", ex.getClass().getSimpleName());
        details.put("message", ex.getMessage());
        details.put("stackTrace", getStackTraceAsString(ex));

        return new ErrorResponseDto("Internal server error", "INTERNAL_ERROR", details);
    }

    private String getStackTraceAsString(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        return stringWriter.toString();
    }

}