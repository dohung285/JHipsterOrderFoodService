package com.dohung.orderfood.exception;

public class ErrorException extends RuntimeException {

    private static final long serialVersionUID = 4125965356358329466L;

    public ErrorException(String message) {
        super(message);
    }
}
