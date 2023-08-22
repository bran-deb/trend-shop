package com.api.oderapi.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.api.oderapi.exceptions.GeneralServiceException;
import com.api.oderapi.exceptions.NoDataFoundException;
import com.api.oderapi.exceptions.ValidateServiceException;
import com.api.oderapi.utilities.WrapperResponse;

import jakarta.security.auth.message.AuthException;
import lombok.extern.slf4j.Slf4j;

//RETORNA LA ESTRUCTURA DE LOS ERRORES AL USUARIO
@Slf4j
@ControllerAdvice
public class ErrorHanlderConfig extends ResponseEntityExceptionHandler {

    // Exception NO CONTROLADA propagado al controller retorna EXCEPTION SIN MESSAGE
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> all(Exception e, WebRequest request) {
        log.error(e.getMessage(), e); // NOTIFICA ERROR NO CONTROLADO
        // NO SABE DE DONDE VIENE EL ERROR POR ESO SE RETORNA EL MESSAGE
        WrapperResponse<?> response = new WrapperResponse<>(false, "Internal Server Error", null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // si la exception se propaga al controller retorna VALIDATESERVICEEXCEPTION
    @ExceptionHandler(ValidateServiceException.class)
    public ResponseEntity<?> validateServiceException(ValidateServiceException e, WebRequest request) {
        log.info(e.getMessage(), e);
        WrapperResponse<?> response = new WrapperResponse<>(false, e.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // si la exception se propaga al controller retorna NODATAFOUNDEXCEPTION
    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<?> noDataFoundException(NoDataFoundException e, WebRequest request) {
        log.info(e.getMessage(), e);
        WrapperResponse<?> response = new WrapperResponse<>(false, e.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // si la exception se propaga al controller retorna GENERALSERVICEEXCEPTION
    @ExceptionHandler(GeneralServiceException.class)
    public ResponseEntity<?> generalServiceException(GeneralServiceException e, WebRequest request) {
        log.error(e.getMessage(), e);
        WrapperResponse<?> response = new WrapperResponse<>(false, "Internal Server Error", null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<?> authException(Exception e, WebRequest request) {
        log.error(e.getMessage(), e); // NOTIFICA ERROR NO CONTROLADO
        // NO SABE DE DONDE VIENE EL ERROR POR ESO SE RETORNA EL MESSAGE
        WrapperResponse<?> response = new WrapperResponse<>(false, "Internal Server Error", null);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
}
