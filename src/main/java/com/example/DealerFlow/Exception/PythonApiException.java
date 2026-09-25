package com.example.DealerFlow.Exception;

import org.springframework.http.HttpStatus;

public class PythonApiException extends RuntimeException {

    private final HttpStatus status;

    public PythonApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public PythonApiException(HttpStatus status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
