package com.api.oderapi.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.oderapi.domain.model.Order;
import com.api.oderapi.domain.model.OrderLine;
import com.api.oderapi.domain.model.Product;
import com.api.oderapi.domain.model.User;
import com.api.oderapi.exceptions.GeneralServiceException;
import com.api.oderapi.exceptions.NoDataFoundException;
import com.api.oderapi.exceptions.ValidateServiceException;
import com.api.oderapi.repository.OrderLineRepository;
import com.api.oderapi.repository.OrderRespository;
import com.api.oderapi.repository.ProductRepository;
import com.api.oderapi.security.UserPrincipal;
import com.api.oderapi.validators.OrderValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderService {

    @Autowired
    private OrderRespository orderRespository;

    @Autowired
    private OrderLineRepository orderLineRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<Order> getAll(Pageable page) {
        log.debug("getAll =>"); // DISPONIBLE EN MODO DEBUGGER
        try {
            // CONVIERTE PAGE<PRODUCT> A LIST<PRODUCT>
            List<Order> orders = orderRespository.findAll(page).toList();
            return orders;

        } catch (ValidateServiceException | NoDataFoundException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new GeneralServiceException(e.getMessage(), e);
        }
    }

    public Order getOrderById(Long id) {
        try {
            Order order = orderRespository.findById(id)
                    .orElseThrow(() -> new NoDataFoundException("No existe la orden con id: " + id));
            return order;

        } catch (ValidateServiceException | NoDataFoundException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new GeneralServiceException(e.getMessage(), e);
        }
    }

    // REPASAR ESTA PARTE
    @Transactional
    public Order postUpdateOrder(Order order) {
        try {
            // VALIDA LAS ORDERS Y ORDERLINES
            OrderValidator.save(order);
            // DESPUES DE VALIDAR LO EXTRAE
            User user = UserPrincipal.getCureentUser();
            // CUENTA EL TOTAL DEPENDIENDO DE EL TOTAL DE LAS LINEAS
            double total = 0;
            // VALIDA QUE EXISTAN LOS PRODUCTOS
            for (OrderLine line : order.getLines()) {
                Product product = productRepository.findById(line.getProduct().getId())
                        .orElseThrow(
                                () -> new NoDataFoundException("No existe el producto " + line.getProduct().getId()));

                // AGREGA LOS CAMPOS BASADOS EN LOS DATOS DE LOS PRODUCTOS EN LA DB
                line.setPrice(product.getPrice());
                line.setTotal(product.getPrice() * line.getQuantity());
                total += line.getTotal();
            }
            // ESTABLECE EL TOTAL DE TODAS LAS LINEAS
            order.setTotal(total);
            // GUARDA EN CASCADE POR LO QUE SE SETEA LAS LINEAS A QUE ORDER PERTENECE
            order.getLines().forEach(line -> line.setOrder(order));

            if (order.getId() == null) {
                order.setUser(user);
                // ESTABLECE LA FECHA QUE VA A TENER EL ORDER
                order.setRegDate(LocalDateTime.now());
                return orderRespository.save(order);
            }
            // VERIFICA QUE EXISTE LA ORDEN PARA ACTUALIZAR
            Order saveOrder = orderRespository.findById(order.getId())
                    .orElseThrow(() -> new NoDataFoundException("No existe la orden con id: " + order.getId()));
            // MATIENE INMUTABLE POR QUE PONEMOS LA FECHA ANTERIOR(orderById)REQUIERE FECHA
            order.setRegDate(saveOrder.getRegDate());
            // order.setId(id); // ESTABLECE EL ID POR PATH A ORDER
            // DEFINE LA LISTA DE LINEAS QUE VAMOS A BORRAR EN BASE A LAS LINEAS GUARDADAS
            List<OrderLine> deletedLines = saveOrder.getLines();
            // ELIMINA TODAS LAS LINEAS QUE ESTAN EN LA ORDEN(LIMPIEZA MANUAL)
            deletedLines.removeAll(order.getLines());
            // ORDERLINE(EQUALS METHOD) PARA COMPARAR Y BORRAR LOS LINE QUE NO TIENEN ID
            orderLineRepository.deleteAll(deletedLines);

            return orderRespository.save(order);

        } catch (ValidateServiceException | NoDataFoundException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new GeneralServiceException(e.getMessage(), e);
        }
    }

    @Transactional
    public void deleteOrder(Long id) {
        try {
            Order orderById = orderRespository.findById(id)
                    .orElseThrow(() -> new NoDataFoundException("No existe el order con id: " + id));
            orderRespository.delete(orderById);

        } catch (ValidateServiceException | NoDataFoundException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new GeneralServiceException(e.getMessage(), e);
        }
    }

}
