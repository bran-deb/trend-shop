package com.api.oderapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.oderapi.domain.model.OrderLine;

public interface OrderLineRepository extends JpaRepository<OrderLine, Long> {

}
