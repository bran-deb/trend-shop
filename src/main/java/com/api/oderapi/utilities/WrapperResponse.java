package com.api.oderapi.utilities;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//SUGERENCIA PERSONAL -> SE USA WRAPPER PARA ESTANDARIZAR RESPONSE
//FORMATO DE EXITO O ERROR
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WrapperResponse<T> {
    // SUGERENCIA PERSONAL ->
    // PARA DECIR SI SE COMPLETO EL PROCESO SE USA "OK"
    private boolean ok;
    private String message;
    private T body;

    public ResponseEntity<WrapperResponse<T>> createResponse() {
        return new ResponseEntity<>(this, HttpStatus.OK);
    }

    public ResponseEntity<WrapperResponse<T>> createResponse(HttpStatus status) {
        return new ResponseEntity<>(this, status);
    }
}
