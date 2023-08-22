package com.api.oderapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Error de datos incorrectos o no procesados
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ValidateServiceException extends RuntimeException {

    public ValidateServiceException() {
        super();
    }

    public ValidateServiceException(String message) {
        super(message);
    }

    public ValidateServiceException(Throwable message) {
        super(message);
    }

    public ValidateServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    protected ValidateServiceException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
