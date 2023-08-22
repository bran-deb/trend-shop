package com.api.oderapi.controller;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.oderapi.converter.OrderConverter;
import com.api.oderapi.domain.model.Order;
import com.api.oderapi.dto.OrderDTO;
import com.api.oderapi.service.OrderService;
import com.api.oderapi.utilities.WrapperResponse;

@RequestMapping("orders")
@RestController
public class OrderController {

    @Autowired
    private OrderConverter converter;

    @Autowired
    private OrderService orderService;

    private static final Logger log = Logger.getLogger(ProductController.class.getCanonicalName());

    @GetMapping({ "", "/" })
    public ResponseEntity<WrapperResponse<List<OrderDTO>>> getAll(
            // PAGINACION DE LA RESPONSE
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageZise", required = false, defaultValue = "5") int pageZise) {
        Pageable page = PageRequest.of(pageNumber, pageZise); // response paginable
        log.info("getAll =>");
        List<Order> orders = orderService.getAll(page);
        List<OrderDTO> orderDTOs = converter.fromEntity(orders);
        WrapperResponse<List<OrderDTO>> response = new WrapperResponse<List<OrderDTO>>(true, "success",
                orderDTOs);

        return response.createResponse(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WrapperResponse<OrderDTO>> getOrderById(@PathVariable Long id) {
        log.info("Get Order By Id =>");
        Order order = orderService.getOrderById(id);
        OrderDTO orderDTO = converter.fromEntity(order);
        WrapperResponse<OrderDTO> response = new WrapperResponse<OrderDTO>(true, "success", orderDTO);

        return response.createResponse();
    }

    @PostMapping("/")
    public ResponseEntity<WrapperResponse<OrderDTO>> postOrder(@RequestBody OrderDTO order) {
        log.info("Post Order =>");
        Order orderEntity = orderService.postUpdateOrder(converter.fromDTO(order));
        OrderDTO orderDTO = converter.fromEntity(orderEntity);
        WrapperResponse<OrderDTO> response = new WrapperResponse<OrderDTO>(true, "success", orderDTO);

        return response.createResponse();
    }

    // REPASAR
    @PutMapping("/")
    public ResponseEntity<WrapperResponse<OrderDTO>> updateOrder(@RequestBody OrderDTO order) {
        log.info("Update Order =>");
        Order orderUpdate = orderService.postUpdateOrder(converter.fromDTO(order));
        OrderDTO orderDTO = converter.fromEntity(orderUpdate);
        WrapperResponse<OrderDTO> response = new WrapperResponse<OrderDTO>(true, "success", orderDTO);

        return response.createResponse();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        log.info("Delete Order =>");
        orderService.deleteOrder(id);
        WrapperResponse<?> response = new WrapperResponse<OrderDTO>(true, "success", null);

        return response.createResponse();
    }

}
