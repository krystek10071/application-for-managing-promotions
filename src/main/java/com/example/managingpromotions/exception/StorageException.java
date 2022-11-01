package com.example.managingpromotions.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StorageException extends RuntimeException {
    private final String message;
}
