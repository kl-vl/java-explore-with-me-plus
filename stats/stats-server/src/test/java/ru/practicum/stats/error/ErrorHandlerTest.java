package ru.practicum.stats.error;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import ru.practicum.stats.ErrorResponseDto;
import ru.practicum.stats.handler.ErrorHandler;

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ErrorHandlerTest {

    @InjectMocks
    private ErrorHandler errorHandler;

    @Mock
    private WebRequest webRequest;

    @Test
    void handleValidationExceptionsShouldReturnValidationErrorResponse() {

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        FieldError fieldError1 = new FieldError("objectName", "field1", "must not be null");
        FieldError fieldError2 = new FieldError("objectName", "field2", "must not be blank");

        when(bindingResult.getFieldErrors()).thenReturn(Arrays.asList(fieldError1, fieldError2));
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(ex.getMessage()).thenReturn("Validation failed");

        when(webRequest.getDescription(false)).thenReturn("uri=/test");

        ErrorResponseDto response = errorHandler.handleValidationExceptions(ex, webRequest);

        assertNotNull(response);
        assertEquals("Validation failed", response.getMessage());
        assertEquals("VALIDATION_ERROR", response.getError());

        Map<String, String> details = response.getDetails();
        assertEquals(2, details.size());
        assertEquals("must not be null", details.get("field1"));
        assertEquals("must not be blank", details.get("field2"));

        verify(webRequest, times(1)).getDescription(false);
    }

    @Test
    void handleValidationExceptionsShouldHandleNullMessage() {

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        FieldError fieldError = new FieldError("objectName", "field", null);

        when(bindingResult.getFieldErrors()).thenReturn(Arrays.asList(fieldError));
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(ex.getMessage()).thenReturn("Validation failed");

        when(webRequest.getDescription(false)).thenReturn("uri=/test");

        ErrorResponseDto response = errorHandler.handleValidationExceptions(ex, webRequest);

        assertNotNull(response);
        Map<String, String> details = response.getDetails();
        assertEquals("Invalid value", details.get("field"));
    }

    @Test
    void errorHandlerInternalShouldReturnInternalErrorResponse() {

        Exception ex = new RuntimeException("Test exception message");
        when(webRequest.getDescription(false)).thenReturn("uri=/test");

        ErrorResponseDto response = errorHandler.errorHandlerInternal(ex, webRequest);

        assertNotNull(response);
        assertEquals("Internal server error", response.getMessage());
        assertEquals("INTERNAL_ERROR", response.getError());

        Map<String, String> details = response.getDetails();
        assertEquals("RuntimeException", details.get("exception"));
        assertEquals("Test exception message", details.get("message"));
        assertNotNull(details.get("stackTrace"));
        assertTrue(details.get("stackTrace").contains("RuntimeException"));

        verify(webRequest, times(1)).getDescription(false);
    }

    @Test
    void errorHandlerInternalShouldHandleExceptionWithoutMessage() {

        Exception ex = new RuntimeException();
        when(webRequest.getDescription(false)).thenReturn("uri=/test");

        ErrorResponseDto response = errorHandler.errorHandlerInternal(ex, webRequest);

        assertNotNull(response);
        Map<String, String> details = response.getDetails();
        assertEquals("RuntimeException", details.get("exception"));
        assertNull(details.get("message"));
        assertNotNull(details.get("stackTrace"));
    }
}