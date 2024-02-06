package com.ar.apartmentrent.exception;

import com.ar.apartmentrent.model.FieldValidationErrorModel;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class RestControllerExceptionHandler {
    @ExceptionHandler(RequestBodyFieldValidationException.class)
    public ResponseEntity<List<FieldValidationErrorModel>> handleRequestBodyFieldValdationException(RequestBodyFieldValidationException exception){
        return new ResponseEntity<>(exception.getFieldValidationErrors(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
