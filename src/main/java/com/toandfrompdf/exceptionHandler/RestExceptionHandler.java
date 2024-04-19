package com.toandfrompdf.exceptionHandler;

import com.toandfrompdf.exceptions.ImageFormatChangeException;
import com.toandfrompdf.exceptions.IncorrectFileFormat;
import com.toandfrompdf.models.ErrorResponse;
import com.toandfrompdf.models.FieldError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice(annotations = RestController.class)
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException exception) {
        final BindingResult bindingResult = exception.getBindingResult();
        final List<FieldError> fieldErrors = bindingResult.getFieldErrors()
                .stream()
                .map(error -> {
                    return FieldError.builder()
                                     .errorCode(error.getCode())
                                             .field(error.getField())
                                                     .errorMessage(error.getDefaultMessage()).build();
                })
                .toList();
        ErrorResponse errorResponse = ErrorResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST.value())
                .fieldErrors(new ArrayList<>())
                .message(exception.getMessage())
                .exception(exception.getClass().getSimpleName())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({IncorrectFileFormat.class, ImageFormatChangeException.class,IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> handleClientSideException(
            final Exception exception) {


         ErrorResponse errorResponse = ErrorResponse.builder()
                 .httpStatus(HttpStatus.BAD_REQUEST.value())
                         .fieldErrors(new ArrayList<>())
                                 .message(exception.getMessage())
                                         .exception(exception.getClass().getSimpleName())
                                                 .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
    @ExceptionHandler({IOException.class})
    public ResponseEntity<ErrorResponse> handleRedundantData(
            final Exception exception) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .fieldErrors(new ArrayList<>())
                .message(exception.getMessage())
                .exception(exception.getClass().getSimpleName())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
