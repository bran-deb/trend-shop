package com.api.oderapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class OrderLineDTO {
    private Long id;
    // private OrderDTO order; //ORDER YA TIENE RELACION A LINE
    private ProductDTO product;
    private Double price;
    private Double quantity;
    private Double total;
}
