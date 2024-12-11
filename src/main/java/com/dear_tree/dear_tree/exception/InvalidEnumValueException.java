package com.dear_tree.dear_tree.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidEnumValueException extends IllegalArgumentException {
    public InvalidEnumValueException(String message) {
        super(message);
    }
}
