package com.api.oderapi.validators;

import com.api.oderapi.domain.model.Order;
import com.api.oderapi.domain.model.OrderLine;
import com.api.oderapi.exceptions.ValidateServiceException;

public class OrderValidator {

    public static void save(Order order) {

        if (order.getLines() == null || order.getLines().isEmpty())
            throw new ValidateServiceException("Las lineas son requeridas");

        // YA NO SALE NULLPOINTEREXCEPTION POR LA VERIFICACION DE ORDER
        for (OrderLine line : order.getLines()) {
            if (line.getProduct() == null || line.getProduct().getId() == null) {
                throw new ValidateServiceException("El producto es requerido");
            }
            if (line.getQuantity() == null) {
                throw new ValidateServiceException("La cantidad es requerido");
            }
            if (line.getQuantity() < 0) {
                throw new ValidateServiceException("La cantidad es incorrecto");
            }
        }
    }
}
