package com.api.oderapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.oderapi.domain.model.Order;

public interface OrderRespository extends JpaRepository<Order, Long> {

}
