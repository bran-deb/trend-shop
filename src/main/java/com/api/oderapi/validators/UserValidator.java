package com.api.oderapi.validators;

import com.api.oderapi.domain.model.User;
import com.api.oderapi.exceptions.ValidateServiceException;

public class UserValidator {

    public static void signup(User user) {

        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new ValidateServiceException("El nombre de usuario es requerido");
        }

        if (user.getUsername().length() > 30) {
            throw new ValidateServiceException("El nombre de usuario solo debe contener max 30 caracteres");
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new ValidateServiceException("El password es requerido");
        }

        // VALIDA QUE EL PASSWORD MAX30 PERO PUEDE ALMACENAR MAX150 POR EL HASHPASSWORD
        if (user.getPassword().length() > 30) {
            throw new ValidateServiceException("El password solo debe contener max 150 caracteres");
        }
    }
}
