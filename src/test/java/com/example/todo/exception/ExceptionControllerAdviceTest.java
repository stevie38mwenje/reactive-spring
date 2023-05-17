package com.example.todo.exception;


import com.example.todo.domain.dto.response.GenericResponse;
import io.r2dbc.spi.R2dbcDataIntegrityViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)

class ExceptionControllerAdviceTest {
    @InjectMocks
    private ExceptionControllerAdvice exceptionControllerAdvice;

    @Test
    void handleIllegalArgumentException_ShouldReturnErrorResponse() {
        String exceptionMessage = "One or more fields are null";
        R2dbcDataIntegrityViolationException r2dbcDataIntegrityViolationException = mock(R2dbcDataIntegrityViolationException.class);
        Mockito.when(r2dbcDataIntegrityViolationException.getMessage()).thenReturn(exceptionMessage);

        GenericResponse response = exceptionControllerAdvice.handleR2dbcDataIntegrityViolationException(r2dbcDataIntegrityViolationException);

        // Verify the response values
        assertEquals(400, response.getStatus());
        assertEquals(null, response.getData());
        assertFalse(response.isSuccess());
    }

}