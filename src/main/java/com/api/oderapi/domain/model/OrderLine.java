package com.api.oderapi.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.GenerationType.IDENTITY;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Builder
@Table(name = "ORDER_LINES")
public class OrderLine {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    // ORDERLINE PERTENECE A UN ORDER
    // ORDER PUEDE CONTENER VARIAS ORDERLINES
    // JoinColumn genera la PK de ORDER como FK
    // @JoinColumn(name = "id_marca", referencedColumnName = "id_marca", nullable =
    // false)
    @ManyToOne
    @JoinColumn(name = "FK_ORDER", nullable = false)
    private Order order;

    // una linea puede tener un producto
    // un producto puede pertenecer a varias lineas
    // JoinColumn genera la PK de PRODUCT como FK
    @ManyToOne
    @JoinColumn(name = "FK_PRODUCT", nullable = false)
    private Product product;

    @Column(name = "PRICE", nullable = false)
    private Double price;

    @Column(name = "QUANTITY", nullable = false)
    private Double quantity;

    @Column(name = "TOTAL", nullable = false)
    private Double total;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OrderLine other = (OrderLine) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
