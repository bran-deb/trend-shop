package com.api.oderapi.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class OrderDTO {
    private Long id;
    // NO SE MANDA LA FECHA YA QUE ES GENERADA POR EL BACKEND
    private String regDate; // DTO no trabaja con (LocalDateTime)
    List<OrderLineDTO> lines; // DTO no pueden contener entity(OrderLine)
    private Double total;
    private UserDTO user;

}
