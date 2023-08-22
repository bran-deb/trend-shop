package com.api.oderapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.oderapi.domain.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    // poniendo find spring reconoce que quieres hacer una busqueda
    // y al poner el nombre del campo te genera la busqueda del campo
    public Optional<User> findByUsername(String username);

}
