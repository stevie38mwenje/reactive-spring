package com.example.todo.exception;

import com.example.todo.domain.dto.response.GenericResponse;
import io.r2dbc.spi.R2dbcDataIntegrityViolationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(R2dbcDataIntegrityViolationException.class)
    public GenericResponse handleR2dbcDataIntegrityViolationException(R2dbcDataIntegrityViolationException ex) {
        String errorMessage = ex.getLocalizedMessage();
        GenericResponse response = new GenericResponse();
        response.setMessage(errorMessage);
        response.setData(null);
        response.setSuccess(false);
        response.setStatus(400);
        return response;
    }

    @ExceptionHandler(CustomException.class)
    public GenericResponse handleCustomException(CustomException ex) {
        String errorMessage = ex.getLocalizedMessage();
        GenericResponse response = new GenericResponse();
        response.setMessage(errorMessage);
        response.setData(null);
        response.setSuccess(false);
        response.setStatus(400);
        return response;
    }
}
