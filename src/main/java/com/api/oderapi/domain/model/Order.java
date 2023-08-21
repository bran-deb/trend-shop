package com.api.oderapi.domain.model;

import static jakarta.persistence.CascadeType.ALL;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.GenerationType.IDENTITY;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "ORDERS")
public class Order {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "REG_DATE", nullable = false, updatable = false)
    private LocalDateTime regDate;

    // ORDER AL TENER UNA LISTA DE LINES NO PUEDE ALMACENAR TODAS LAS LINEAS
    // ORDER PUEDE TENER VARIOS LINES Y UN LINE SOLO UN ORDER

    // mappedBy para vincular como se llama ORDER dentro de ORDERLINE
    // cascade para que las consultas apliquen tambien a las llaves foraneas

    // las lineas no se cargan hasta que se hace un GETLINES por eso se usa fetch
    // FETCH cuando consulte la ORDEN hace (innerjoin) a LINES y carga las lineas
    @OneToMany(mappedBy = "order", cascade = ALL, fetch = FetchType.EAGER)
    private List<OrderLine> lines;

    @Column(name = "TOTAL", nullable = false)
    private Double total;

    // una orden tiene un usuario y un usuario puede crear varias ordenes
    // JoinColumn genera la PK de USER como FK
    // updatable=false para que no se modifique el campo
    @ManyToOne
    @JoinColumn(name = "FK_USER", updatable = false)
    private User user;

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
        Order other = (Order) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
