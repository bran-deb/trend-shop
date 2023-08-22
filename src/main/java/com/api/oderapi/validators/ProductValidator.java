package com.api.oderapi.validators;

import com.api.oderapi.domain.model.Product;
import com.api.oderapi.exceptions.ValidateServiceException;

public class ProductValidator {

    public static void save(Product product) {

        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new ValidateServiceException("El nombre es requerido");
        }
        if (product.getName().length() >= 30) {
            throw new ValidateServiceException("El nombre es muy largo");
        }
        if (product.getPrice() == null) {
            throw new ValidateServiceException("El precio es requerido");
        }
        if (product.getPrice() < 0) {
            throw new ValidateServiceException("El precio no puede ser negativo");
        }
    }
}
