package com.api.oderapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Errores de contenido
@ResponseStatus(code = HttpStatus.NO_CONTENT)
public class NoDataFoundException extends RuntimeException {

    public NoDataFoundException() {
        super();
    }

    public NoDataFoundException(String message) {
        super(message);
    }

    public NoDataFoundException(Throwable message) {
        super(message);
    }

    public NoDataFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    protected NoDataFoundException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
