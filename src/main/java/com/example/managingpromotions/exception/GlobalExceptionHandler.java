package com.example.managingpromotions.exception;

import com.example.managingpromotions.util.ErrorResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.managingPromotions.api.model.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception ex) {
        final ErrorResponse errorResponse = ErrorResponseUtils.createInternalServerErrorResponse();

        log.error(errorResponse.getDescription(), ex);
        errorResponse.setDescription(ex.getMessage());
        return errorResponse;
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(UserNotFoundException ex) {
        String description = "Not found User: " + ex.getUserName() + "in the database";
        log.error(description);

        return ErrorResponseUtils.createErrorResponse(HttpStatus.NOT_FOUND.value(), description, description);
    }
}
