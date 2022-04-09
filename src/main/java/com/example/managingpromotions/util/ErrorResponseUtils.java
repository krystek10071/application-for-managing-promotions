package com.example.managingpromotions.util;

import com.example.managingpromotions.exception.ExternalApplicationException;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import pl.managingPromotions.api.model.ErrorResponse;

@UtilityClass
public class ErrorResponseUtils {

    public ErrorResponse getErrorResponse(ExternalApplicationException ex) {
        ErrorResponse errorResponse;
        if (ex.getErrorResponse() != null) {
            errorResponse = ex.getErrorResponse();
        } else {
            errorResponse = createInternalServerErrorResponse();
        }
        return errorResponse;
    }

    public ErrorResponse createInternalServerErrorResponse() {
        return createErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Something went wrong while processing the http request",
                null
        );
    }

    public ErrorResponse createErrorResponse(int code, String description, String id) {
        final ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setId(id);
        errorResponse.setCode(code);
        errorResponse.setDescription(description);
        return errorResponse;
    }
}
