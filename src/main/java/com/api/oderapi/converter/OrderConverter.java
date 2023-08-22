package com.api.oderapi.converter;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import com.api.oderapi.domain.model.Order;
import com.api.oderapi.domain.model.OrderLine;
import com.api.oderapi.dto.OrderDTO;
import com.api.oderapi.dto.OrderLineDTO;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OrderConverter extends AbstractConverter<Order, OrderDTO> {

    private DateTimeFormatter dateTimeFormat;
    private ProductConverter productConverter;
    private UserConverter userConverter;

    @Override
    public OrderDTO fromEntity(Order entity) {
        if (entity == null) {
            return null;
        }
        List<OrderLineDTO> lines = fromOrderLineEntity(entity.getLines());

        return OrderDTO.builder()
                .id(entity.getId())
                .regDate(entity.getRegDate().format(dateTimeFormat))// de date a string
                .lines(lines)
                .total(entity.getTotal())
                .user(userConverter.fromEntity(entity.getUser()))
                .build();
    }

    @Override
    public Order fromDTO(OrderDTO dto) {
        if (dto == null) {
            return null;
        }
        List<OrderLine> lines = fromOrderLineDTO(dto.getLines());

        return Order.builder()
                .id(dto.getId())
                // .registerDate() // No se recibe fecha de creaccion por que se crea en back
                .lines(lines)
                .total(dto.getTotal())
                .user(userConverter.fromDTO(dto.getUser()))
                .build();
    }

    // ORDERLINES CONVERTERS
    private List<OrderLineDTO> fromOrderLineEntity(List<OrderLine> lines) {
        if (lines == null) {
            return null;
        }

        return lines.stream()
                .map(line -> {
                    return OrderLineDTO.builder()
                            .id(line.getId())
                            .price(line.getPrice())
                            .product(productConverter.fromEntity(line.getProduct()))
                            .quantity(line.getQuantity())
                            .total(line.getTotal())
                            .build();
                }).collect(Collectors.toList());
    }

    private List<OrderLine> fromOrderLineDTO(List<OrderLineDTO> lines) {
        if (lines == null) {
            return null;
        }

        return lines.stream()
                .map(line -> {
                    return OrderLine.builder()
                            .id(line.getId())
                            .price(line.getPrice())
                            .product(productConverter.fromDTO(line.getProduct()))
                            .quantity(line.getQuantity())
                            .total(line.getTotal())
                            .build();
                }).collect(Collectors.toList());
    }
}
