package com.example.managingpromotions.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.managingPromotions.api.model.ErrorResponse;

@Getter
@AllArgsConstructor
public class ExternalApplicationException extends RuntimeException {
    private final ErrorResponse errorResponse;
}
