package com.dani.epigramapi.exception;

public class DuplicateEpigramException extends RuntimeException {
    public DuplicateEpigramException(String message) {
        super(message);
    }
}