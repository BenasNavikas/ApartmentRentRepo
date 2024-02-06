package com.ar.apartmentrent.exception;

import com.ar.apartmentrent.model.FieldValidationErrorModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestBodyFieldValidationException extends RuntimeException{
    private List<FieldValidationErrorModel> fieldValidationErrors;

    public RequestBodyFieldValidationException(List<FieldValidationErrorModel> fieldValidationErrors){
        super();
        this.fieldValidationErrors = fieldValidationErrors;
    }
}
